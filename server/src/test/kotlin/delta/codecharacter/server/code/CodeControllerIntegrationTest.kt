package delta.codecharacter.server.code

import com.fasterxml.jackson.databind.ObjectMapper
import delta.codecharacter.dtos.CodeDto
import delta.codecharacter.dtos.CodeRevisionDto
import delta.codecharacter.dtos.CodeTypeDto
import delta.codecharacter.dtos.CreateCodeRevisionRequestDto
import delta.codecharacter.dtos.LanguageDto
import delta.codecharacter.dtos.UpdateLatestCodeRequestDto
import delta.codecharacter.server.TestAttributes
import delta.codecharacter.server.WithMockCustomUser
import delta.codecharacter.server.code.code_revision.CodeRevisionEntity
import delta.codecharacter.server.code.latest_code.LatestCodeEntity
import delta.codecharacter.server.code.locked_code.LockedCodeEntity
import delta.codecharacter.server.user.UserEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.dropCollection
import org.springframework.data.mongodb.core.findAll
import org.springframework.http.MediaType
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@AutoConfigureMockMvc
@SpringBootTest
internal class CodeControllerIntegrationTest(@Autowired val mockMvc: MockMvc) {
    @Autowired private lateinit var mongoTemplate: MongoTemplate

    @Autowired private lateinit var jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        mapper = jackson2ObjectMapperBuilder.build()
        mongoTemplate.save<UserEntity>(TestAttributes.user)
    }

    @Test
    @WithMockCustomUser
    fun `should create code revision`() {

        val dto =
            CreateCodeRevisionRequestDto(code = "code", message = "message", language = LanguageDto.CPP)

        mockMvc
            .post("/user/code/revisions") {
                content = mapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
            }
            .andExpect { status { is2xxSuccessful() } }

        val codeRevisions = mongoTemplate.findAll<CodeRevisionEntity>()
        assertThat(codeRevisions.size).isEqualTo(1)

        val codeRevision = codeRevisions.first()
        assertThat(codeRevision.code).isEqualTo(dto.code)
        assertThat(codeRevision.message).isEqualTo(dto.message)
        assertThat(codeRevision.language).isEqualTo(LanguageEnum.valueOf(dto.language.name))
        assertThat(codeRevision.userId).isEqualTo(TestAttributes.user.id)
    }

    @Test
    @WithMockCustomUser
    fun `should get code revisions`() {
        val codeRevisionEntity =
            CodeRevisionEntity(
                id = UUID.randomUUID(),
                code = "code",
                message = "message",
                language = LanguageEnum.CPP,
                userId = TestAttributes.user.id,
                codeType = CodeTypeDto.NORMAL,
                parentRevision = null,
                createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            )
        mongoTemplate.insert<CodeRevisionEntity>(codeRevisionEntity)

        val expectedDto =
            listOf(
                CodeRevisionDto(
                    id = codeRevisionEntity.id,
                    code = codeRevisionEntity.code,
                    message = codeRevisionEntity.message,
                    language = LanguageDto.CPP,
                    createdAt = codeRevisionEntity.createdAt
                )
            )

        mockMvc.get("/user/code/revisions") { contentType = MediaType.APPLICATION_JSON }.andExpect {
            status { is2xxSuccessful() }
            content { json(mapper.writeValueAsString(expectedDto)) }
        }
    }

    @Test
    @WithMockCustomUser
    fun `should get latest code`() {
        val code = HashMap<CodeTypeDto, Code>()
        code[CodeTypeDto.NORMAL] =
            Code(
                code = "code",
                language = LanguageEnum.CPP,
                lastSavedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            )
        val latestCodeEntity = LatestCodeEntity(userId = TestAttributes.user.id, latestCode = code)
        mongoTemplate.insert<LatestCodeEntity>(latestCodeEntity)
        val expectedDto =
            CodeDto(
                code = latestCodeEntity.latestCode[CodeTypeDto.NORMAL]?.code.toString(),
                language = LanguageDto.CPP,
                lastSavedAt = latestCodeEntity.latestCode[CodeTypeDto.NORMAL]?.lastSavedAt
                    ?: Instant.MIN
            )

        mockMvc.get("/user/code/latest") { contentType = MediaType.APPLICATION_JSON }.andExpect {
            status { is2xxSuccessful() }
            content { json(mapper.writeValueAsString(expectedDto)) }
        }
    }

    @Test
    @WithMockCustomUser
    fun `should update latest code`() {
        val oldCode = HashMap<CodeTypeDto, Code>()
        oldCode[CodeTypeDto.NORMAL] =
            Code(
                code = "code",
                language = LanguageEnum.CPP,
                lastSavedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            )
        val oldCodeEntity = LatestCodeEntity(userId = TestAttributes.user.id, latestCode = oldCode)
        mongoTemplate.insert<LatestCodeEntity>(oldCodeEntity)

        val dto =
            UpdateLatestCodeRequestDto(
                code = "import sys", language = LanguageDto.PYTHON, codeType = CodeTypeDto.NORMAL
            )
        mockMvc
            .post("/user/code/latest") {
                content = mapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
            }
            .andExpect { status { is2xxSuccessful() } }

        val latestCode = mongoTemplate.findAll<LatestCodeEntity>().first()
        assertThat(latestCode.latestCode[CodeTypeDto.NORMAL]?.code).isEqualTo(dto.code)
        assertThat(latestCode.latestCode[CodeTypeDto.NORMAL]?.language)
            .isEqualTo(LanguageEnum.valueOf(dto.language.name))
        assertThat(latestCode.latestCode[CodeTypeDto.NORMAL]?.lastSavedAt)
            .isNotEqualTo(oldCodeEntity.latestCode[CodeTypeDto.NORMAL]?.lastSavedAt)
    }

    @Test
    @WithMockCustomUser
    fun `should update latest code with lock`() {
        val oldCode = HashMap<CodeTypeDto, Code>()
        oldCode[CodeTypeDto.NORMAL] =
            Code(
                code = "code",
                language = LanguageEnum.CPP,
                lastSavedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            )
        val oldCodeEntity = LatestCodeEntity(userId = TestAttributes.user.id, latestCode = oldCode)
        mongoTemplate.insert<LatestCodeEntity>(oldCodeEntity)

        val dto =
            UpdateLatestCodeRequestDto(
                code = "import sys",
                language = LanguageDto.PYTHON,
                lock = true,
                codeType = CodeTypeDto.NORMAL
            )

        mockMvc
            .post("/user/code/latest") {
                content = mapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
            }
            .andExpect { status { is2xxSuccessful() } }

        val latestCode = mongoTemplate.findAll<LatestCodeEntity>().first()
        assertThat(latestCode.latestCode[CodeTypeDto.NORMAL]?.code).isEqualTo(dto.code)
        assertThat(latestCode.latestCode[CodeTypeDto.NORMAL]?.language)
            .isEqualTo(LanguageEnum.valueOf(dto.language.name))
        assertThat(latestCode.latestCode[CodeTypeDto.NORMAL]?.lastSavedAt)
            .isNotEqualTo(oldCodeEntity.latestCode[CodeTypeDto.NORMAL]?.lastSavedAt)

        val lockedCode = mongoTemplate.findAll<LockedCodeEntity>().first()
        assertThat(lockedCode.lockedCode[CodeTypeDto.NORMAL]?.code).isEqualTo(dto.code)
        assertThat(lockedCode.lockedCode[CodeTypeDto.NORMAL]?.language)
            .isEqualTo(LanguageEnum.valueOf(dto.language.name))
    }

    @AfterEach
    fun tearDown() {
        mongoTemplate.dropCollection<CodeRevisionEntity>()
        mongoTemplate.dropCollection<LatestCodeEntity>()
        mongoTemplate.dropCollection<LockedCodeEntity>()
        mongoTemplate.dropCollection<UserEntity>()
    }
}
