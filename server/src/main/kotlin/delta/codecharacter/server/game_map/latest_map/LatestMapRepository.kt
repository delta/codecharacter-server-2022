package delta.codecharacter.server.game_map.latest_map

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/** Repository for [LatestMapEntity] */
@Repository interface LatestMapRepository : MongoRepository<LatestMapEntity, UUID>
