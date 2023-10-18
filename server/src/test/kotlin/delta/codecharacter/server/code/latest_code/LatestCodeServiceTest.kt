package delta.codecharacter.server.code.latest_code

import delta.codecharacter.dtos.CodeTypeDto
import delta.codecharacter.dtos.LanguageDto
import delta.codecharacter.dtos.UpdateLatestCodeRequestDto
import delta.codecharacter.server.code.Code
import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.config.DefaultCodeMapConfiguration
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Optional
import java.util.UUID

internal class LatestCodeServiceTest {
    private lateinit var latestCodeRepository: LatestCodeRepository
    private lateinit var latestCodeService: LatestCodeService
    private lateinit var defaultCodeMapConfiguration: DefaultCodeMapConfiguration

    @BeforeEach
    fun setUp() {
        latestCodeRepository = mockk()
        defaultCodeMapConfiguration = mockk()
        latestCodeService = LatestCodeService(latestCodeRepository, defaultCodeMapConfiguration)
    }

    @Test
    fun `should return latest code`() {
        val code = HashMap<CodeTypeDto, Code>()
        code[CodeTypeDto.NORMAL] =
            Code(
                code = "code",
                language = LanguageEnum.CPP,
                lastSavedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            )
        val userId = UUID.randomUUID()
        val latestCodeEntity = LatestCodeEntity(userId = userId, latestCode = code)

        every { defaultCodeMapConfiguration.defaultCode } returns "code"
        every { defaultCodeMapConfiguration.defaultLanguage } returns LanguageEnum.CPP
        every { defaultCodeMapConfiguration.defaultLatestCode } returns
            Code(code = "code", language = LanguageEnum.CPP, lastSavedAt = Instant.MIN)

        every { latestCodeRepository.findById(userId) } returns Optional.of(latestCodeEntity)

        val latestCode = latestCodeService.getLatestCode(userId, CodeTypeDto.NORMAL)

        verify { latestCodeRepository.findById(userId) }
        confirmVerified(latestCodeRepository)
        assertNotNull(latestCode)
    }

    @Test
    fun `should update latest code`() {
        val code = HashMap<CodeTypeDto, Code>()
        code[CodeTypeDto.NORMAL] =
            Code(
                code = "code",
                language = LanguageEnum.CPP,
                lastSavedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            )
        val userId = UUID.randomUUID()
        val latestCodeEntity = LatestCodeEntity(userId = userId, latestCode = code)
        val codeDto =
            UpdateLatestCodeRequestDto(
                code = latestCodeEntity.latestCode[CodeTypeDto.NORMAL]?.code.toString(),
                language = LanguageDto.CPP
            )

        every { latestCodeRepository.save(any()) } returns latestCodeEntity
        every { latestCodeRepository.findById(userId) } returns Optional.of(latestCodeEntity)
        latestCodeService.updateLatestCode(userId, codeDto)
        verify { latestCodeRepository.save(any()) }
        verify { latestCodeRepository.findById(userId) }
        confirmVerified(latestCodeRepository)
    }
}
