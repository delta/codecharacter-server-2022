package delta.codecharacter.server.code.locked_code

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
import java.util.Optional

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
        val user = mockk<UserEntity>()
        val lockedCodeEntity = LockedCodeEntity(code = "code", language = LanguageEnum.C, user = user)

        every { lockedCodeRepository.findById(user) } returns Optional.of(lockedCodeEntity)

        val latestCode = lockedCodeService.getLockedCode(user)

        verify { lockedCodeRepository.findById(user) }
        confirmVerified(lockedCodeRepository)
        assertNotNull(latestCode)
    }

    @Test
    fun `should update latest code`() {
        val user = mockk<UserEntity>()
        val lockedCodeEntity = LockedCodeEntity(code = "code", language = LanguageEnum.C, user = user)
        val codeDto = UpdateLatestCodeRequestDto(code = lockedCodeEntity.code, language = LanguageDto.C)

        every { lockedCodeRepository.save(any()) } returns lockedCodeEntity

        lockedCodeService.updateLockedCode(user, codeDto)

        verify { lockedCodeRepository.save(any()) }
        confirmVerified(lockedCodeRepository)
    }
}
