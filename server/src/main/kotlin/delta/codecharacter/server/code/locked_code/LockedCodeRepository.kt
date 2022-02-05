package delta.codecharacter.server.code.locked_code

import delta.codecharacter.server.user.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/** Repository for [LockedCodeEntity] */
@Repository interface LockedCodeRepository : MongoRepository<LockedCodeEntity, UserEntity>
