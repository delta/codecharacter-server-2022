package delta.codecharacter.server.user.rating_history

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface RatingHistoryRepository : MongoRepository<RatingHistoryEntity, UUID> {
    fun findAllByUserId(userId: UUID): List<RatingHistoryEntity>
}
