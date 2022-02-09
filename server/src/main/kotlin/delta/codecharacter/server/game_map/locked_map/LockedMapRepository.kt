package delta.codecharacter.server.game_map.locked_map

import delta.codecharacter.server.user.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/** Repository for [LockedMapEntity] */
@Repository interface LockedMapRepository : MongoRepository<LockedMapEntity, UserEntity>
