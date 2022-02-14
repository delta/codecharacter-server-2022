package delta.codecharacter.server

import delta.codecharacter.server.user.UserEntity
import delta.codecharacter.server.user.public_user.PublicUserEntity
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory
import java.util.UUID

class TestAttributes {
    companion object {
        val user =
            UserEntity(
                id = UUID.randomUUID(),
                username = "user",
                password = "password",
                email = "user@test.com",
                isEnabled = true,
                isAccountNonExpired = true,
                isAccountNonLocked = true,
            )
        val publicUser =
            PublicUserEntity(
                userId = user.id,
                username = user.username,
                name = "Test User",
                country = "Test Country",
                college = "Test College",
                avatarId = 1,
                rating = 1000.0,
                wins = 4,
                losses = 2,
                ties = 1,
            )
    }
}

@Configuration
class SecurityTestConfiguration {
    @Bean
    fun rabbitAdmin(connectionFactory: ConnectionFactory): RabbitAdmin {
        return RabbitAdmin(connectionFactory)
    }
}

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory::class)
annotation class WithMockCustomUser

class WithMockCustomUserSecurityContextFactory : WithSecurityContextFactory<WithMockCustomUser> {
    override fun createSecurityContext(customUser: WithMockCustomUser): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()
        val principal = TestAttributes.user
        val auth: Authentication =
            UsernamePasswordAuthenticationToken(
                principal, TestAttributes.user.password, principal.authorities
            )
        context.authentication = auth
        return context
    }
}
