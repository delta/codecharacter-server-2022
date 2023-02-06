package delta.codecharacter.server.game_map.latest_map

import delta.codecharacter.dtos.GameMapTypeDto
import delta.codecharacter.server.game_map.GameMap
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

/**
 * Latest map entity.
 *
 * @param userId
 * @param latestMap
 */
@Document(collection = "latest_map")
data class LatestMapEntity(
    @Id val userId: UUID,
    val latestMap: HashMap<GameMapTypeDto, GameMap>,
)
