package delta.codecharacter.server.config

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableRabbit
class QueueConfiguration {
    @Bean fun gameRequestQueue() = Queue("gameRequestQueue")
    @Bean fun gameStatusUpdateQueue() = Queue("gameStatusUpdateQueue")
}
