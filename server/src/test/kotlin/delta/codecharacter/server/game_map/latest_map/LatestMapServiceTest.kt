package delta.codecharacter.server.game_map.latest_map

import delta.codecharacter.dtos.UpdateLatestMapRequestDto
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

internal class LatestMapServiceTest {
    private lateinit var latestMapRepository: LatestMapRepository
    private lateinit var latestMapService: LatestMapService

    @BeforeEach
    fun setUp() {
        latestMapRepository = mockk()
        latestMapService = LatestMapService(latestMapRepository)
    }

    @Test
    fun `should return latest map`() {
        val userId = UUID.randomUUID()
        val latestMapEntity =
            LatestMapEntity(map = "[[0]]", userId = userId, lastSavedAt = Instant.now())

        every { latestMapRepository.findById(userId) } returns Optional.of(latestMapEntity)

        val latestMap = latestMapService.getLatestMap(userId)

        verify { latestMapRepository.findById(userId) }
        confirmVerified(latestMapRepository)
        assertNotNull(latestMap)
    }

    @Test
    fun `should update latest map`() {
        val userId = UUID.randomUUID()
        val latestMapEntity =
            LatestMapEntity(map = "[[0]]", userId = userId, lastSavedAt = Instant.now())
        val mapDto = UpdateLatestMapRequestDto(map = latestMapEntity.map)

        every { latestMapRepository.save(any()) } returns latestMapEntity

        latestMapService.updateLatestMap(userId, mapDto)

        verify { latestMapRepository.save(any()) }
        confirmVerified(latestMapRepository)
    }
}
