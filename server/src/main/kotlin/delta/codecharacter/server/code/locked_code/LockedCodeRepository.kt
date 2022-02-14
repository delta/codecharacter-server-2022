package delta.codecharacter.server.code.locked_code

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/** Repository for [LockedCodeEntity] */
@Repository interface LockedCodeRepository : MongoRepository<LockedCodeEntity, UUID>
