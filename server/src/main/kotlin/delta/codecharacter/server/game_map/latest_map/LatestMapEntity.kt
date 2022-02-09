package delta.codecharacter.server.game_map.latest_map

import delta.codecharacter.server.user.UserEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

/**
 * Latest map entity.
 *
 * @param user
 * @param map
 * @param lastSavedAt
 */
@Document(collection = "latest_map")
data class LatestMapEntity(
    @Id val user: UserEntity,
    val map: String,
    val lastSavedAt: Instant,
)
