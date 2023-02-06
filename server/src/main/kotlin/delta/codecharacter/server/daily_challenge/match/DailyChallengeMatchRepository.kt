package delta.codecharacter.server.daily_challenge.match

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DailyChallengeMatchRepository : MongoRepository<DailyChallengeMatchEntity, UUID>
