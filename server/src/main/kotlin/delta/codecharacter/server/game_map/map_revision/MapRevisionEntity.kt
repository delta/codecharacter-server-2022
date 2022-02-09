package delta.codecharacter.server.game_map.map_revision

import delta.codecharacter.server.user.UserEntity
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
 * @param parentRevision
 * @param user
 * @param createdAt
 */
@Document(collection = "map_revision")
data class MapRevisionEntity(
    @Id val id: UUID,
    val map: String,
    @DocumentReference(lazy = true) val parentRevision: MapRevisionEntity?,
    @DocumentReference(lazy = true) val user: UserEntity,
    val createdAt: Instant
)
