package delta.codecharacter.server.game_map.map_revision

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

/** Repository for [MapRevisionEntity] */
@Repository
interface MapRevisionRepository : MongoRepository<MapRevisionEntity, UUID> {
    fun findAllByUserIdOrderByCreatedAtDesc(userId: UUID): List<MapRevisionEntity>
    fun findFirstByUserIdOrderByCreatedAtDesc(userId: UUID): Optional<MapRevisionEntity>
}
