package delta.codecharacter.server.code.locked_code

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

internal class LockedCodeServiceTest {
    private lateinit var lockedCodeRepository: LockedCodeRepository
    private lateinit var lockedCodeService: LockedCodeService
    private lateinit var defaultCodeMapConfiguration: DefaultCodeMapConfiguration

    @BeforeEach
    fun setUp() {
        lockedCodeRepository = mockk()
        defaultCodeMapConfiguration = mockk()
        lockedCodeService = LockedCodeService(lockedCodeRepository, defaultCodeMapConfiguration)
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
        val lockedCodeEntity = LockedCodeEntity(userId = userId, lockedCode = code)

        every { defaultCodeMapConfiguration.defaultCode } returns "code"
        every { defaultCodeMapConfiguration.defaultLanguage } returns LanguageEnum.CPP
        every { defaultCodeMapConfiguration.defaultLockedCode } returns
            Code(code = "code", language = LanguageEnum.CPP)

        every { lockedCodeRepository.findById(userId) } returns Optional.of(lockedCodeEntity)

        val latestCode = lockedCodeService.getLockedCode(userId)

        verify { lockedCodeRepository.findById(userId) }
        confirmVerified(lockedCodeRepository)
        assertNotNull(latestCode)
    }

    @Test
    fun `should update latest code`() {
        val userId = UUID.randomUUID()
        val code = HashMap<CodeTypeDto, Code>()
        code[CodeTypeDto.NORMAL] = Code(code = "code", language = LanguageEnum.CPP)
        val lockedCodeEntity = LockedCodeEntity(userId = userId, lockedCode = code)
        val codeDto =
            UpdateLatestCodeRequestDto(
                code = lockedCodeEntity.lockedCode[CodeTypeDto.NORMAL]?.code.toString(),
                language = LanguageDto.C
            )

        every { lockedCodeRepository.save(any()) } returns lockedCodeEntity
        every { lockedCodeRepository.findById(userId) } returns Optional.of(lockedCodeEntity)
        lockedCodeService.updateLockedCode(userId, codeDto)
        verify { lockedCodeRepository.findById(userId) }
        verify { lockedCodeRepository.save(any()) }
        confirmVerified(lockedCodeRepository)
    }
}
