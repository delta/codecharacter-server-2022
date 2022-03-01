package delta.codecharacter.server.code.latest_code

import delta.codecharacter.dtos.LanguageDto
import delta.codecharacter.dtos.UpdateLatestCodeRequestDto
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
        val userId = UUID.randomUUID()
        val latestCodeEntity =
            LatestCodeEntity(
                code = "code", language = LanguageEnum.C, userId = userId, lastSavedAt = Instant.now()
            )

        every { defaultCodeMapConfiguration.defaultCode } returns "code"
        every { defaultCodeMapConfiguration.defaultLanguage } returns LanguageEnum.C
        every { latestCodeRepository.findById(userId) } returns Optional.of(latestCodeEntity)

        val latestCode = latestCodeService.getLatestCode(userId)

        verify { latestCodeRepository.findById(userId) }
        confirmVerified(latestCodeRepository)
        assertNotNull(latestCode)
    }

    @Test
    fun `should update latest code`() {
        val userId = UUID.randomUUID()
        val latestCodeEntity =
            LatestCodeEntity(
                code = "code", language = LanguageEnum.C, userId = userId, lastSavedAt = Instant.now()
            )
        val codeDto = UpdateLatestCodeRequestDto(code = latestCodeEntity.code, language = LanguageDto.C)

        every { latestCodeRepository.save(any()) } returns latestCodeEntity

        latestCodeService.updateLatestCode(userId, codeDto)

        verify { latestCodeRepository.save(any()) }
        confirmVerified(latestCodeRepository)
    }
}
