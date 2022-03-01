package delta.codecharacter.server.game_map.locked_map

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
        val lockedMapEntity = LockedMapEntity(map = "map", userId = userId)

        every { defaultCodeMapConfiguration.defaultMap } returns "[[0]]"
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
        every { mapValidator.validateMap(lockedMapEntity.map) } returns Unit

        lockedMapService.updateLockedMap(userId, mapDto)

        verify { lockedMapRepository.save(any()) }
        verify { mapValidator.validateMap(lockedMapEntity.map) }
        confirmVerified(lockedMapRepository, mapValidator)
    }
}
