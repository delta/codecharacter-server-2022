package delta.codecharacter.server.code.locked_code

import delta.codecharacter.dtos.LanguageDto
import delta.codecharacter.dtos.UpdateLatestCodeRequestDto
import delta.codecharacter.server.code.LanguageEnum
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional
import java.util.UUID

internal class LockedCodeServiceTest {
    private lateinit var lockedCodeRepository: LockedCodeRepository
    private lateinit var lockedCodeService: LockedCodeService

    @BeforeEach
    fun setUp() {
        lockedCodeRepository = mockk()
        lockedCodeService = LockedCodeService(lockedCodeRepository)
    }

    @Test
    fun `should return latest code`() {
        val userId = UUID.randomUUID()
        val lockedCodeEntity =
            LockedCodeEntity(code = "code", language = LanguageEnum.C, userId = userId)

        every { lockedCodeRepository.findById(userId) } returns Optional.of(lockedCodeEntity)

        val latestCode = lockedCodeService.getLockedCode(userId)

        verify { lockedCodeRepository.findById(userId) }
        confirmVerified(lockedCodeRepository)
        assertNotNull(latestCode)
    }

    @Test
    fun `should update latest code`() {
        val userId = UUID.randomUUID()
        val lockedCodeEntity =
            LockedCodeEntity(code = "code", language = LanguageEnum.C, userId = userId)
        val codeDto = UpdateLatestCodeRequestDto(code = lockedCodeEntity.code, language = LanguageDto.C)

        every { lockedCodeRepository.save(any()) } returns lockedCodeEntity

        lockedCodeService.updateLockedCode(userId, codeDto)

        verify { lockedCodeRepository.save(any()) }
        confirmVerified(lockedCodeRepository)
    }
}
