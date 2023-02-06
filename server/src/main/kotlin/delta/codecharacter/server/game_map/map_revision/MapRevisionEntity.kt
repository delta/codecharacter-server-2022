package delta.codecharacter.server.game_map.map_revision

import delta.codecharacter.dtos.GameMapTypeDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.time.Instant
import java.util.UUID

/**
 * Map Revision Entity
 *
 * @param id
 * @param map
 * @param mapImage
 * @param mapType
 * @param message
 * @param parentRevision
 * @param userId
 * @param createdAt
 */
@Document(collection = "map_revision")
data class MapRevisionEntity(
    @Id val id: UUID,
    val map: String,
    val mapImage: String,
    val mapType: GameMapTypeDto,
    val message: String,
    @DocumentReference(lazy = true) val parentRevision: MapRevisionEntity?,
    val userId: UUID,
    val createdAt: Instant
)
