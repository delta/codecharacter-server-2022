package delta.codecharacter.server.game_map.map_revision

import delta.codecharacter.dtos.CreateMapRevisionRequestDto
import delta.codecharacter.dtos.GameMapRevisionDto
import delta.codecharacter.server.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

/** Service for map revision. */
@Service
class MapRevisionService(@Autowired private val mapRevisionRepository: MapRevisionRepository) {

    fun createMapRevision(
        userEntity: UserEntity,
        createMapRevisionRequestDto: CreateMapRevisionRequestDto
    ) {
        val (map) = createMapRevisionRequestDto
        val parentCodeRevision =
            mapRevisionRepository.findFirstByUserOrderByCreatedAtDesc(userEntity).orElse(null)
        mapRevisionRepository.save(
            MapRevisionEntity(
                id = UUID.randomUUID(),
                map = map,
                user = userEntity,
                parentRevision = parentCodeRevision,
                createdAt = Instant.now()
            )
        )
    }

    fun getMapRevisions(userEntity: UserEntity): List<GameMapRevisionDto> {
        return mapRevisionRepository.findAllByUserOrderByCreatedAtDesc(userEntity).map {
            GameMapRevisionDto(
                id = it.id,
                map = it.map,
                parentRevision = it.parentRevision?.id,
                createdAt = it.createdAt
            )
        }
    }
}
