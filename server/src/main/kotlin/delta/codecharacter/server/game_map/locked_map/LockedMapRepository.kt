package delta.codecharacter.server.game_map.locked_map

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/** Repository for [LockedMapEntity] */
@Repository interface LockedMapRepository : MongoRepository<LockedMapEntity, UUID>
