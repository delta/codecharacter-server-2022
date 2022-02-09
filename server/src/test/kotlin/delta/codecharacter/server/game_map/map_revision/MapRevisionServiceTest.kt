package delta.codecharacter.server.game_map.map_revision

import delta.codecharacter.dtos.CreateMapRevisionRequestDto
import delta.codecharacter.server.user.UserEntity
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.Optional
import java.util.UUID

internal class MapRevisionServiceTest {
    private lateinit var mapRevisionRepository: MapRevisionRepository
    private lateinit var mapRevisionService: MapRevisionService

    @BeforeEach
    fun setUp() {
        mapRevisionRepository = mockk()
        mapRevisionService = MapRevisionService(mapRevisionRepository)
    }

    @Test
    fun `should create map revision`() {
        val user = mockk<UserEntity>()
        val createMapRevisionRequestDto =
            CreateMapRevisionRequestDto(
                map = "map",
            )
        val mapRevisionEntity = mockk<MapRevisionEntity>()

        every { mapRevisionRepository.findFirstByUserOrderByCreatedAtDesc(user) } returns
            Optional.of(mapRevisionEntity)
        every { mapRevisionRepository.save(any()) } returns mapRevisionEntity

        mapRevisionService.createMapRevision(user, createMapRevisionRequestDto)

        verify { mapRevisionRepository.findFirstByUserOrderByCreatedAtDesc(user) }
        verify { mapRevisionRepository.save(any()) }

        confirmVerified(mapRevisionRepository)
    }

    @Test
    fun `should get all map revisions`() {
        val user = mockk<UserEntity>()
        val mapRevisionEntity =
            MapRevisionEntity(
                id = UUID.randomUUID(),
                user = user,
                map = "map",
                parentRevision = null,
                createdAt = Instant.now(),
            )

        every { mapRevisionRepository.findAllByUserOrderByCreatedAtDesc(user) } returns
            listOf(mapRevisionEntity)

        val mapRevisionDtos = mapRevisionService.getMapRevisions(user)
        val mapRevisionDto = mapRevisionDtos.first()

        verify { mapRevisionRepository.findAllByUserOrderByCreatedAtDesc(user) }

        confirmVerified(mapRevisionRepository)
        assert(mapRevisionDtos.size == 1)
        assert(mapRevisionDto.id == mapRevisionEntity.id)
        assert(mapRevisionDto.map == mapRevisionEntity.map)
        assert(mapRevisionDto.parentRevision == mapRevisionEntity.parentRevision?.id)
    }
}
