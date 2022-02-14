package delta.codecharacter.server.game_map.latest_map

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.UUID

/**
 * Latest map entity.
 *
 * @param userId
 * @param map
 * @param lastSavedAt
 */
@Document(collection = "latest_map")
data class LatestMapEntity(
    @Id val userId: UUID,
    val map: String,
    val lastSavedAt: Instant,
)
