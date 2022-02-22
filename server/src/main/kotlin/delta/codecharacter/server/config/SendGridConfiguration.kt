package delta.codecharacter.server.config

import com.sendgrid.SendGrid
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SendGridConfiguration {
    @Value("\${spring.sendgrid.api-key}") private lateinit var key: String
    @Bean
    fun getSendGrid(): SendGrid {
        return SendGrid(key)
    }
}
