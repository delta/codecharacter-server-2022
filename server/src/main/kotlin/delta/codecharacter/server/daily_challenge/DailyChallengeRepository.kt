package delta.codecharacter.server.daily_challenge

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository interface DailyChallengeRepository : MongoRepository<DailyChallengeEntity, UUID>
