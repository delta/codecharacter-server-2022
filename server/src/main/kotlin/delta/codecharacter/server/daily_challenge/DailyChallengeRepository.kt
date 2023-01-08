package delta.codecharacter.server.daily_challenge

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface DailyChallengeRepository : MongoRepository<DailyChallengeEntity, UUID> {
    fun findByDay(day: Int): Optional<DailyChallengeEntity>
}
