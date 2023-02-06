package delta.codecharacter.server.game_map.map_revision

import delta.codecharacter.dtos.GameMapTypeDto
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

/** Repository for [MapRevisionEntity] */
@Repository
interface MapRevisionRepository : MongoRepository<MapRevisionEntity, UUID> {
    fun findAllByUserIdAndMapTypeOrderByCreatedAtDesc(
        userId: UUID,
        mapType: GameMapTypeDto
    ): List<MapRevisionEntity>
    fun findFirstByUserIdAndMapTypeOrderByCreatedAtDesc(
        userId: UUID,
        mapType: GameMapTypeDto
    ): Optional<MapRevisionEntity>

    fun findByUserIdAndId(userId: UUID, commitId: UUID): Optional<MapRevisionEntity>
}
