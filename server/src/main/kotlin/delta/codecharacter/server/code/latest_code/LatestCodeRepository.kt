package delta.codecharacter.server.code.latest_code

import delta.codecharacter.server.user.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/** Repository for [LatestCodeEntity] */
@Repository interface LatestCodeRepository : MongoRepository<LatestCodeEntity, UserEntity>
