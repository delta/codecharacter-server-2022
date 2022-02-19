package delta.codecharacter.server.notifications

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.Instant
import java.util.UUID

internal class NotificationServiceTest {

    private lateinit var notificationRepository: NotificationRepository

    private lateinit var notificationService: NotificationService

    private val jackson2ObjectMapperBuilder = Jackson2ObjectMapperBuilder()

    @BeforeEach
    fun setUp() {
        notificationRepository = mockk()
        notificationService =
            NotificationService(notificationRepository, jackson2ObjectMapperBuilder, mockk())
    }

    @Test
    fun `should get all notifications of user`() {
        val userId = UUID.randomUUID()
        val notificationEntity =
            NotificationEntity(
                id = UUID.randomUUID(),
                userId = userId,
                title = "title",
                content = "content",
                createdAt = Instant.now(),
                read = false
            )

        every { notificationRepository.findAllByUserId(userId) } returns listOf(notificationEntity)

        val notifications = notificationService.getAllNotifications(userId)
        assertThat(notifications).hasSize(1)

        val notification = notifications.first()
        assertThat(notification.id).isEqualTo(notificationEntity.id)
        assertThat(notification.title).isEqualTo(notificationEntity.title)

        verify { notificationRepository.findAllByUserId(userId) }
        confirmVerified(notificationRepository)
    }

    @Test
    fun `should save notification read status`() {
        val userId = UUID.randomUUID()
        val notificationId = UUID.randomUUID()
        val notificationEntity =
            NotificationEntity(
                id = notificationId,
                userId = userId,
                title = "title",
                content = "content",
                createdAt = Instant.now(),
                read = false
            )

        every { notificationRepository.findAllByUserId(userId) } returns listOf(notificationEntity)
        every { notificationRepository.save(any()) } returns mockk()

        notificationService.saveNotificationReadStatus(userId, notificationId, true)

        verify { notificationRepository.findAllByUserId(userId) }
        verify { notificationRepository.save(any()) }
        confirmVerified(notificationRepository)
    }
}
