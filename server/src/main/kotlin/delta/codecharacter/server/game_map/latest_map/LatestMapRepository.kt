package delta.codecharacter.server.game_map.latest_map

import delta.codecharacter.server.user.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/** Repository for [LatestMapEntity] */
@Repository interface LatestMapRepository : MongoRepository<LatestMapEntity, UserEntity>
