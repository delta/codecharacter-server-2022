package delta.codecharacter.server

import delta.codecharacter.server.user.UserEntity
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
            )
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
