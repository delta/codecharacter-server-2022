package delta.codecharacter.server.user.rating_history

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface RatingHistoryRepository : MongoRepository<RatingHistoryEntity, String> {
    fun findAllByUserId(userId: UUID): List<RatingHistoryEntity>
    fun findFirstByUserIdOrderByValidFromDesc(userId: UUID): RatingHistoryEntity
    fun findFirstByUserIdInOrderByValidFromDesc(userIds: List<UUID>): List<RatingHistoryEntity>
}
