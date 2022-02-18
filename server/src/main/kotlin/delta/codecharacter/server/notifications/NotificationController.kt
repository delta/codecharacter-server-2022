package delta.codecharacter.server.notifications

import delta.codecharacter.core.NotificationApi
import delta.codecharacter.dtos.NotificationDto
import delta.codecharacter.server.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class NotificationController(@Autowired private val notificationService: NotificationService) :
    NotificationApi {
    override fun getAllNotifications(): ResponseEntity<List<NotificationDto>> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        return ResponseEntity.ok(notificationService.getAllNotifications(user.id))
    }

    override fun saveNotificationReadStatus(
        notificationId: UUID,
        body: Boolean
    ): ResponseEntity<Unit> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        notificationService.saveNotificationReadStatus(user.id, notificationId, body)
        return ResponseEntity.ok().build()
    }
}
