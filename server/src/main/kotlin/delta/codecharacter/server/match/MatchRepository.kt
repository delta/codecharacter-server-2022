package delta.codecharacter.server.match

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MatchRepository : MongoRepository<MatchEntity, UUID> {
    fun findTop10ByOrderByTotalPointsDesc(): List<MatchEntity>
}
