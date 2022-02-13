package delta.codecharacter.server.code.latest_code

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/** Repository for [LatestCodeEntity] */
@Repository interface LatestCodeRepository : MongoRepository<LatestCodeEntity, UUID>
