package delta.codecharacter.server.notifications

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.UUID

@Document(collection = "notification")
data class NotificationEntity(
    @Id val id: UUID,
    val title: String,
    val content: String,
    val createdAt: Instant,
    val read: Boolean,
    val userId: UUID
)
