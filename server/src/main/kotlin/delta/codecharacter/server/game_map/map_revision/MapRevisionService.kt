package delta.codecharacter.server.game_map.map_revision

import delta.codecharacter.dtos.CreateMapRevisionRequestDto
import delta.codecharacter.dtos.GameMapRevisionDto
import delta.codecharacter.server.logic.validation.MapValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

/** Service for map revision. */
@Service
class MapRevisionService(
    @Autowired private val mapRevisionRepository: MapRevisionRepository,
    @Autowired private val mapValidator: MapValidator,
) {

    fun createMapRevision(userId: UUID, createMapRevisionRequestDto: CreateMapRevisionRequestDto) {
        val (map, message) = createMapRevisionRequestDto
        mapValidator.validateMap(map)
        val parentCodeRevision =
            mapRevisionRepository.findFirstByUserIdOrderByCreatedAtDesc(userId).orElse(null)
        mapRevisionRepository.save(
            MapRevisionEntity(
                id = UUID.randomUUID(),
                map = map,
                message = message,
                userId = userId,
                parentRevision = parentCodeRevision,
                createdAt = Instant.now()
            )
        )
    }

    fun getMapRevisions(userId: UUID): List<GameMapRevisionDto> {
        return mapRevisionRepository.findAllByUserIdOrderByCreatedAtDesc(userId).map {
            GameMapRevisionDto(
                id = it.id,
                map = it.map,
                message = it.message,
                parentRevision = it.parentRevision?.id,
                createdAt = it.createdAt
            )
        }
    }
}
