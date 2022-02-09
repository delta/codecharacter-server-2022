package delta.codecharacter.server.game_map.locked_map

import delta.codecharacter.dtos.UpdateLatestMapRequestDto
import delta.codecharacter.server.user.UserEntity
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional

internal class LockedMapServiceTest {
    private lateinit var lockedMapRepository: LockedMapRepository
    private lateinit var lockedMapService: LockedMapService

    @BeforeEach
    fun setUp() {
        lockedMapRepository = mockk()
        lockedMapService = LockedMapService(lockedMapRepository)
    }

    @Test
    fun `should return latest map`() {
        val user = mockk<UserEntity>()
        val lockedMapEntity = LockedMapEntity(map = "map", user = user)

        every { lockedMapRepository.findById(user) } returns Optional.of(lockedMapEntity)

        val latestMap = lockedMapService.getLockedMap(user)

        verify { lockedMapRepository.findById(user) }
        confirmVerified(lockedMapRepository)
        assertNotNull(latestMap)
    }

    @Test
    fun `should update latest map`() {
        val user = mockk<UserEntity>()
        val lockedMapEntity = LockedMapEntity(map = "map", user = user)
        val mapDto = UpdateLatestMapRequestDto(map = lockedMapEntity.map)

        every { lockedMapRepository.save(any()) } returns lockedMapEntity

        lockedMapService.updateLockedMap(user, mapDto)

        verify { lockedMapRepository.save(any()) }
        confirmVerified(lockedMapRepository)
    }
}
