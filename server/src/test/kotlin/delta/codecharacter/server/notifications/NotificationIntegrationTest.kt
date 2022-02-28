package delta.codecharacter.server.notifications

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import delta.codecharacter.dtos.NotificationDto
import delta.codecharacter.server.TestAttributes
import delta.codecharacter.server.user.UserEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.dropCollection
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.messaging.converter.StringMessageConverter
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotificationIntegrationTest(@Autowired val mockMvc: MockMvc) {

    @Value("\${local.server.port}") private val port = 0

    @Autowired private lateinit var notificationService: NotificationService

    @Autowired private lateinit var mongoTemplate: MongoTemplate

    private lateinit var stompClient: WebSocketStompClient
    private lateinit var stompSession: StompSession

    @Autowired private lateinit var jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        mapper = jackson2ObjectMapperBuilder.build()

        mongoTemplate.save<UserEntity>(TestAttributes.user)

        stompClient = WebSocketStompClient(StandardWebSocketClient())
        stompClient.messageConverter = StringMessageConverter()

        stompSession =
            stompClient
                .connect("ws://localhost:$port/ws", object : StompSessionHandlerAdapter() {})
                .get()
    }

    @Test
    fun `should receive user notifications`() {
        val user = TestAttributes.user

        var message: String? = null
        stompSession.subscribe(
            "/notifications/${user.id}", ClientFrameHandler { payload -> message = payload }
        )
        notificationService.sendNotification(user.id, "test", "test")

        Thread.sleep(100)
        assertThat(message).isNotNull

        val notification = mapper.readValue<NotificationDto>(message!!)
        assertThat(notification.title).isEqualTo("test")
        assertThat(notification.content).isEqualTo("test")
    }

    @AfterEach
    fun tearDown() {
        mongoTemplate.dropCollection<UserEntity>()
        mongoTemplate.dropCollection<NotificationEntity>()

        stompSession.disconnect()
        stompClient.stop()
    }
}
