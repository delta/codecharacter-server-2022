package delta.codecharacter.server.notifications

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface NotificationRepository : MongoRepository<NotificationEntity, UUID> {
    fun findAllByUserId(userId: UUID): List<NotificationEntity>
}
