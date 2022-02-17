package delta.codecharacter.server.notifications

import delta.codecharacter.server.TestAttributes
import delta.codecharacter.server.user.UserEntity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.dropCollection
import org.springframework.messaging.converter.StringMessageConverter
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import org.springframework.web.socket.sockjs.client.SockJsClient
import org.springframework.web.socket.sockjs.client.WebSocketTransport

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotificationIntegrationTest(@Autowired val mockMvc: MockMvc) {

    @Value("\${local.server.port}") private val port = 0

    @Autowired private lateinit var notificationService: NotificationService

    @Autowired private lateinit var mongoTemplate: MongoTemplate

    private lateinit var stompClient: WebSocketStompClient
    private lateinit var stompSession: StompSession

    @BeforeEach
    fun setUp() {
        mongoTemplate.save<UserEntity>(TestAttributes.user)

        stompClient =
            WebSocketStompClient(SockJsClient(listOf(WebSocketTransport(StandardWebSocketClient()))))
        stompClient.messageConverter = StringMessageConverter()

        stompSession =
            stompClient
                .connect("ws://localhost:$port/ws", object : StompSessionHandlerAdapter() {})
                .get()
    }

    @Test
    fun `should receive user notifications`() {
        val user = TestAttributes.user

        stompSession.subscribe("/notifications", ClientFrameHandler { payload -> println(payload) })
        Thread.sleep(1000)
        notificationService.sendNotification(user.id, "test", "test")
        Thread.sleep(1000)
    }

    @AfterEach
    fun tearDown() {
        mongoTemplate.dropCollection<UserEntity>()
        mongoTemplate.dropCollection<NotificationEntity>()

        stompSession.disconnect()
        stompClient.stop()
    }
}
