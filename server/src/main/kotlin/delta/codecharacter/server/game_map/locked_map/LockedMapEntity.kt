package delta.codecharacter.server.game_map.locked_map

import delta.codecharacter.server.user.UserEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Locked map entity.
 *
 * @param user
 * @param map
 */
@Document(collection = "locked_map")
data class LockedMapEntity(@Id val user: UserEntity, val map: String)
