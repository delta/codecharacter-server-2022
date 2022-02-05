package delta.codecharacter.server.code.latest_code

import delta.codecharacter.dtos.LanguageDto
import delta.codecharacter.dtos.UpdateLatestCodeRequestDto
import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.user.UserEntity
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.Optional

internal class LatestCodeServiceTest {
    private lateinit var latestCodeRepository: LatestCodeRepository
    private lateinit var latestCodeService: LatestCodeService

    @BeforeEach
    fun setUp() {
        latestCodeRepository = mockk()
        latestCodeService = LatestCodeService(latestCodeRepository)
    }

    @Test
    fun `should return latest code`() {
        val user = mockk<UserEntity>()
        val latestCodeEntity =
            LatestCodeEntity(
                code = "code", language = LanguageEnum.C, user = user, lastSavedAt = Instant.now()
            )

        every { latestCodeRepository.findById(user) } returns Optional.of(latestCodeEntity)

        val latestCode = latestCodeService.getLatestCode(user)

        verify { latestCodeRepository.findById(user) }
        confirmVerified(latestCodeRepository)
        assertNotNull(latestCode)
    }

    @Test
    fun `should update latest code`() {
        val user = mockk<UserEntity>()
        val latestCodeEntity =
            LatestCodeEntity(
                code = "code", language = LanguageEnum.C, user = user, lastSavedAt = Instant.now()
            )
        val codeDto = UpdateLatestCodeRequestDto(code = latestCodeEntity.code, language = LanguageDto.C)

        every { latestCodeRepository.save(any()) } returns latestCodeEntity

        latestCodeService.updateLatestCode(user, codeDto)

        verify { latestCodeRepository.save(any()) }
        confirmVerified(latestCodeRepository)
    }
}
