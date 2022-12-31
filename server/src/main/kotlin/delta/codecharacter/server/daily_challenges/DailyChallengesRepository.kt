package delta.codecharacter.server.daily_challenges

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface DailyChallengesRepository : MongoRepository<DailyChallengeEntity, UUID> {
}
