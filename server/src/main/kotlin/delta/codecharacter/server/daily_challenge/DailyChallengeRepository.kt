package delta.codecharacter.server.daily_challenge

import delta.codecharacter.server.game.DailyChallengeEntity
import delta.codecharacter.server.game.GameEntity
import delta.codecharacter.server.user.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository interface DailyChallengeRepository : MongoRepository<DailyChallengeEntity, UUID> {
    fun findFirstByDate(date: Date): Optional<DailyChallengeEntity>
}
