package delta.codecharacter.server.game_map.locked_map

import delta.codecharacter.dtos.GameMapTypeDto
import delta.codecharacter.server.game_map.GameMap
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

/**
 * Locked map entity.
 *
 * @param userId
 * @param lockedMap
 */
@Document(collection = "locked_map")
data class LockedMapEntity(@Id val userId: UUID, val lockedMap: HashMap<GameMapTypeDto, GameMap>)
