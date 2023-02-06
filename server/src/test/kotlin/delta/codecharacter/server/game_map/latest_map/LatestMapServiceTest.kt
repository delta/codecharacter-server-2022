package delta.codecharacter.server.game_map.latest_map

import delta.codecharacter.dtos.GameMapTypeDto
import delta.codecharacter.dtos.UpdateLatestMapRequestDto
import delta.codecharacter.server.config.DefaultCodeMapConfiguration
import delta.codecharacter.server.game_map.GameMap
import delta.codecharacter.server.logic.validation.MapValidator
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
        val map = HashMap<GameMapTypeDto, GameMap>()
        map[GameMapTypeDto.NORMAL] =
            GameMap(
                map = "map",
                mapImage = "base64",
                lastSavedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            )
        val latestMapEntity = LatestMapEntity(userId = userId, latestMap = map)

        every { defaultCodeMapConfiguration.defaultMap } returns "[[0]]"
        every { defaultCodeMapConfiguration.defaultMapImage } returns "base64"
        every { defaultCodeMapConfiguration.defaultLatestGameMap } returns
            GameMap(mapImage = "base64", map = "[[0]]", lastSavedAt = Instant.MIN)
        every { latestMapRepository.findById(userId) } returns Optional.of(latestMapEntity)

        val latestMap = latestMapService.getLatestMap(userId)

        verify { latestMapRepository.findById(userId) }
        confirmVerified(latestMapRepository)
        assertNotNull(latestMap)
    }

    @Test
    fun `should update latest map`() {
        val userId = UUID.randomUUID()
        val oldlatestMap = HashMap<GameMapTypeDto, GameMap>()
        oldlatestMap[GameMapTypeDto.NORMAL] =
            GameMap(
                map = "map",
                mapImage = "base64",
                lastSavedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            )
        val latestMapEntity = LatestMapEntity(userId = userId, latestMap = oldlatestMap)
        val mapDto =
            UpdateLatestMapRequestDto(
                map = latestMapEntity.latestMap[GameMapTypeDto.NORMAL]?.map.toString(), mapImage = ""
            )

        every { latestMapRepository.save(any()) } returns latestMapEntity
        every {
            mapValidator.validateMap(latestMapEntity.latestMap[GameMapTypeDto.NORMAL]?.map.toString())
        } returns Unit
        every { latestMapRepository.findById(userId) } returns Optional.of(latestMapEntity)
        latestMapService.updateLatestMap(userId, mapDto)
        verify { latestMapRepository.findById(userId) }
        verify { latestMapRepository.save(any()) }
        verify {
            mapValidator.validateMap(latestMapEntity.latestMap[GameMapTypeDto.NORMAL]?.map.toString())
        }
        confirmVerified(latestMapRepository, mapValidator)
    }
}
