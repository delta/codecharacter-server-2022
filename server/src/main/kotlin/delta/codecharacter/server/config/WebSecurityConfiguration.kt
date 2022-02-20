package delta.codecharacter.server.config

import delta.codecharacter.server.auth.JwtRequestFilter
import delta.codecharacter.server.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired private lateinit var jwtRequestFilter: JwtRequestFilter
    @Autowired private lateinit var userService: UserService

    @Value("\${cors.enabled}") private val corsEnabled: Boolean = false

    override fun configure(http: HttpSecurity?) {
        http {
            csrf { disable() }
            authorizeRequests { authorize(HttpMethod.OPTIONS, "/**", permitAll) }
            cors { if (!corsEnabled) disable() }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtRequestFilter)
        }
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userService)?.passwordEncoder(passwordEncoder())
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
    @Bean fun passwordEncoder() = BCryptPasswordEncoder()
}
