package delta.codecharacter.server.daily_challenge.match

import delta.codecharacter.server.user.public_user.PublicUserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DailyChallengeMatchRepository : MongoRepository<DailyChallengeMatchEntity, UUID> {
    fun findByUserOrderByCreatedAtDesc(user: PublicUserEntity): List<DailyChallengeMatchEntity>
}
