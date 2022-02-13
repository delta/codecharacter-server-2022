package delta.codecharacter.server.code.code_revision

import delta.codecharacter.dtos.CreateCodeRevisionRequestDto
import delta.codecharacter.dtos.LanguageDto
import delta.codecharacter.server.code.LanguageEnum
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.Optional
import java.util.UUID

internal class CodeRevisionServiceTest {
    private lateinit var codeRevisionRepository: CodeRevisionRepository
    private lateinit var codeRevisionService: CodeRevisionService

    @BeforeEach
    fun setUp() {
        codeRevisionRepository = mockk()
        codeRevisionService = CodeRevisionService(codeRevisionRepository)
    }

    @Test
    fun `should create code revision`() {
        val userId = UUID.randomUUID()
        val createCodeRevisionRequestDto =
            CreateCodeRevisionRequestDto(
                code = "code",
                language = LanguageDto.C,
            )
        val codeRevisionEntity = mockk<CodeRevisionEntity>()

        every { codeRevisionRepository.findFirstByUserIdOrderByCreatedAtDesc(userId) } returns
            Optional.of(codeRevisionEntity)
        every { codeRevisionRepository.save(any()) } returns codeRevisionEntity

        codeRevisionService.createCodeRevision(userId, createCodeRevisionRequestDto)

        verify { codeRevisionRepository.findFirstByUserIdOrderByCreatedAtDesc(userId) }
        verify { codeRevisionRepository.save(any()) }

        confirmVerified(codeRevisionRepository)
    }

    @Test
    fun `should get all code revisions`() {
        val userId = UUID.randomUUID()
        val codeRevisionEntity =
            CodeRevisionEntity(
                id = UUID.randomUUID(),
                userId = userId,
                code = "code",
                language = LanguageEnum.C,
                parentRevision = null,
                createdAt = Instant.now(),
            )

        every { codeRevisionRepository.findAllByUserIdOrderByCreatedAtDesc(userId) } returns
            listOf(codeRevisionEntity)

        val codeRevisionDtos = codeRevisionService.getCodeRevisions(userId)
        val codeRevisionDto = codeRevisionDtos.first()

        verify { codeRevisionRepository.findAllByUserIdOrderByCreatedAtDesc(userId) }

        confirmVerified(codeRevisionRepository)
        assert(codeRevisionDtos.size == 1)
        assert(codeRevisionDto.id == codeRevisionEntity.id)
        assert(codeRevisionDto.code == codeRevisionEntity.code)
        assert(codeRevisionDto.language.name == codeRevisionEntity.language.name)
        assert(codeRevisionDto.parentRevision == codeRevisionEntity.parentRevision?.id)
        assert(codeRevisionDto.createdAt == codeRevisionEntity.createdAt)
    }
}
