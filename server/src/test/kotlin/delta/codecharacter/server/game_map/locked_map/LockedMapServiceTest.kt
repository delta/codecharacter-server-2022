package delta.codecharacter.server.game_map.locked_map

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
import java.util.Optional
import java.util.UUID

internal class LockedMapServiceTest {
    private lateinit var lockedMapRepository: LockedMapRepository
    private lateinit var lockedMapService: LockedMapService
    private lateinit var defaultCodeMapConfiguration: DefaultCodeMapConfiguration
    private lateinit var mapValidator: MapValidator

    @BeforeEach
    fun setUp() {
        lockedMapRepository = mockk()
        mapValidator = mockk()
        defaultCodeMapConfiguration = mockk()
        lockedMapService =
            LockedMapService(lockedMapRepository, defaultCodeMapConfiguration, mapValidator)
    }

    @Test
    fun `should return latest map`() {
        val userId = UUID.randomUUID()
        val lockedMap = HashMap<GameMapTypeDto, GameMap>()
        lockedMap[GameMapTypeDto.NORMAL] = GameMap(map = "map", mapImage = "base64")
        val lockedMapEntity = LockedMapEntity(userId = userId, lockedMap = lockedMap)

        every { defaultCodeMapConfiguration.defaultMap } returns "[[0]]"
        every { defaultCodeMapConfiguration.defaultMap } returns "base64"
        every { defaultCodeMapConfiguration.defaultLockedGameMap } returns
            GameMap(mapImage = "base64", map = "[[0]]")

        every { lockedMapRepository.findById(userId) } returns Optional.of(lockedMapEntity)

        val latestMap = lockedMapService.getLockedMap(userId)

        verify { lockedMapRepository.findById(userId) }
        confirmVerified(lockedMapRepository)
        assertNotNull(latestMap)
    }

    @Test
    fun `should update latest map`() {
        val userId = UUID.randomUUID()
        val lockedMap = HashMap<GameMapTypeDto, GameMap>()
        lockedMap[GameMapTypeDto.NORMAL] = GameMap(map = "map", mapImage = "base64")
        val lockedMapEntity = LockedMapEntity(userId = userId, lockedMap = lockedMap)
        val mapDto =
            UpdateLatestMapRequestDto(
                map = lockedMapEntity.lockedMap[GameMapTypeDto.NORMAL]?.map.toString(), mapImage = ""
            )

        every { lockedMapRepository.save(any()) } returns lockedMapEntity
        every {
            mapValidator.validateMap(lockedMapEntity.lockedMap[GameMapTypeDto.NORMAL]?.map.toString())
        } returns Unit
        every { lockedMapRepository.findById(userId) } returns Optional.of(lockedMapEntity)
        lockedMapService.updateLockedMap(userId, mapDto)
        verify { lockedMapRepository.findById(userId) }
        verify { lockedMapRepository.save(any()) }
        verify {
            mapValidator.validateMap(lockedMapEntity.lockedMap[GameMapTypeDto.NORMAL]?.map.toString())
        }
        confirmVerified(lockedMapRepository, mapValidator)
    }
}
