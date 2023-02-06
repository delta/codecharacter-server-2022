package delta.codecharacter.server.game_map.map_revision

import delta.codecharacter.dtos.CreateMapRevisionRequestDto
import delta.codecharacter.dtos.GameMapTypeDto
import delta.codecharacter.server.logic.validation.MapValidator
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.Optional
import java.util.UUID

internal class MapRevisionServiceTest {
    private lateinit var mapRevisionRepository: MapRevisionRepository
    private lateinit var mapRevisionService: MapRevisionService
    private lateinit var mapValidator: MapValidator

    @BeforeEach
    fun setUp() {
        mapRevisionRepository = mockk()
        mapValidator = mockk()
        mapRevisionService = MapRevisionService(mapRevisionRepository, mapValidator)
    }

    @Test
    fun `should create map revision`() {
        val userId = UUID.randomUUID()
        val createMapRevisionRequestDto =
            CreateMapRevisionRequestDto(
                map = "map",
                mapType = GameMapTypeDto.NORMAL,
                mapImage = "",
                message = "message",
            )
        val mapRevisionEntity = mockk<MapRevisionEntity>()

        every {
            mapRevisionRepository.findFirstByUserIdAndMapTypeOrderByCreatedAtDesc(
                userId, GameMapTypeDto.NORMAL
            )
        } returns Optional.of(mapRevisionEntity)
        every { mapRevisionRepository.save(any()) } returns mapRevisionEntity
        every { mapValidator.validateMap(createMapRevisionRequestDto.map) } returns Unit

        mapRevisionService.createMapRevision(userId, createMapRevisionRequestDto)

        verify {
            mapRevisionRepository.findFirstByUserIdAndMapTypeOrderByCreatedAtDesc(
                userId, GameMapTypeDto.NORMAL
            )
        }
        verify { mapValidator.validateMap(createMapRevisionRequestDto.map) }
        verify { mapRevisionRepository.save(any()) }

        confirmVerified(mapRevisionRepository, mapValidator)
    }

    @Test
    fun `should get all map revisions`() {
        val userId = UUID.randomUUID()
        val mapRevisionEntity =
            MapRevisionEntity(
                id = UUID.randomUUID(),
                userId = userId,
                map = "map",
                mapType = GameMapTypeDto.NORMAL,
                mapImage = "",
                message = "message",
                parentRevision = null,
                createdAt = Instant.now(),
            )

        every {
            mapRevisionRepository.findAllByUserIdAndMapTypeOrderByCreatedAtDesc(
                userId, GameMapTypeDto.NORMAL
            )
        } returns listOf(mapRevisionEntity)

        val mapRevisionDtos = mapRevisionService.getMapRevisions(userId)
        val mapRevisionDto = mapRevisionDtos.first()

        verify {
            mapRevisionRepository.findAllByUserIdAndMapTypeOrderByCreatedAtDesc(
                userId, GameMapTypeDto.NORMAL
            )
        }

        confirmVerified(mapRevisionRepository)
        assertThat(mapRevisionDtos.size).isEqualTo(1)
        assertThat(mapRevisionDto.id).isEqualTo(mapRevisionEntity.id)
        assertThat(mapRevisionDto.map).isEqualTo(mapRevisionEntity.map)
        assertThat(mapRevisionDto.parentRevision).isEqualTo(mapRevisionEntity.parentRevision?.id)
    }
}
