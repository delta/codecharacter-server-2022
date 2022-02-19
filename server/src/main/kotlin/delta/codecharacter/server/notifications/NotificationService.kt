package delta.codecharacter.server.notifications

import com.fasterxml.jackson.databind.ObjectMapper
import delta.codecharacter.dtos.NotificationDto
import delta.codecharacter.server.exception.CustomException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class NotificationService(
    @Autowired private val notificationRepository: NotificationRepository,
    @Autowired private val jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder,
    @Autowired private val simpMessagingTemplate: SimpMessagingTemplate,
) {
    private var mapper: ObjectMapper = jackson2ObjectMapperBuilder.build()

    fun getAllNotifications(userId: UUID): List<NotificationDto> {
        return notificationRepository.findAllByUserId(userId).map { notificationEntity ->
            NotificationDto(
                id = notificationEntity.id,
                title = notificationEntity.title,
                content = notificationEntity.content,
                createdAt = notificationEntity.createdAt,
                read = notificationEntity.read
            )
        }
    }

    fun saveNotificationReadStatus(userId: UUID, notificationId: UUID, read: Boolean) {
        val notification =
            notificationRepository.findAllByUserId(userId).find { it.id == notificationId }
                ?: throw CustomException(HttpStatus.NOT_FOUND, "Notification not found")
        notificationRepository.save(notification.copy(read = read))
    }

    fun sendNotification(userId: UUID, title: String, content: String) {
        val notification =
            NotificationEntity(
                id = UUID.randomUUID(),
                userId = userId,
                title = title,
                content = content,
                createdAt = Instant.now(),
                read = false
            )
        notificationRepository.save(notification)
        simpMessagingTemplate.convertAndSend(
            "/notifications/$userId",
            mapper.writeValueAsString(
                NotificationDto(
                    id = notification.id,
                    title = notification.title,
                    content = notification.content,
                    createdAt = notification.createdAt,
                    read = notification.read
                )
            )
        )
    }
}
