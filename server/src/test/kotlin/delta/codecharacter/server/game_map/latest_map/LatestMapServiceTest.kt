package delta.codecharacter.server.game_map.latest_map

import delta.codecharacter.dtos.UpdateLatestMapRequestDto
import delta.codecharacter.server.config.DefaultCodeMapConfiguration
import delta.codecharacter.server.logic.validation.MapValidator
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
    private lateinit var defaultCodeMapConfiguration: DefaultCodeMapConfiguration
    private lateinit var mapValidator: MapValidator

    @BeforeEach
    fun setUp() {
        latestMapRepository = mockk()
        mapValidator = mockk()
        defaultCodeMapConfiguration = mockk()
        latestMapService =
            LatestMapService(latestMapRepository, defaultCodeMapConfiguration, mapValidator)
    }

    @Test
    fun `should return latest map`() {
        val userId = UUID.randomUUID()
        val latestMapEntity =
            LatestMapEntity(map = "[[0]]", userId = userId, lastSavedAt = Instant.now())

        every { defaultCodeMapConfiguration.defaultMap } returns "[[0]]"
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
        every { mapValidator.validateMap(latestMapEntity.map) } returns Unit

        latestMapService.updateLatestMap(userId, mapDto)

        verify { latestMapRepository.save(any()) }
        verify { mapValidator.validateMap(latestMapEntity.map) }
        confirmVerified(latestMapRepository, mapValidator)
    }
}
