package delta.codecharacter.server.game_map.locked_map

import delta.codecharacter.dtos.UpdateLatestMapRequestDto
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional
import java.util.UUID

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
        val userId = UUID.randomUUID()
        val lockedMapEntity = LockedMapEntity(map = "map", userId = userId)

        every { lockedMapRepository.findById(userId) } returns Optional.of(lockedMapEntity)

        val latestMap = lockedMapService.getLockedMap(userId)

        verify { lockedMapRepository.findById(userId) }
        confirmVerified(lockedMapRepository)
        assertNotNull(latestMap)
    }

    @Test
    fun `should update latest map`() {
        val userId = UUID.randomUUID()
        val lockedMapEntity = LockedMapEntity(map = "map", userId = userId)
        val mapDto = UpdateLatestMapRequestDto(map = lockedMapEntity.map)

        every { lockedMapRepository.save(any()) } returns lockedMapEntity

        lockedMapService.updateLockedMap(userId, mapDto)

        verify { lockedMapRepository.save(any()) }
        confirmVerified(lockedMapRepository)
    }
}
