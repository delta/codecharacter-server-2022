package delta.codecharacter.server.config

import delta.codecharacter.server.auth.jwt.JwtRequestFilter
import delta.codecharacter.server.auth.oauth2.CustomOAuth2FailureHandler
import delta.codecharacter.server.auth.oauth2.CustomOAuth2SuccessHandler
import delta.codecharacter.server.auth.oauth2.CustomOAuth2UserService
import delta.codecharacter.server.auth.oauth2.CustomOidcUserService
import delta.codecharacter.server.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.server.invoke
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.firewall.HttpStatusRequestRejectedHandler
import org.springframework.security.web.firewall.RequestRejectedHandler

@Configuration
class WebSecurityConfiguration {

    @Autowired private lateinit var jwtRequestFilter: JwtRequestFilter
    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var customOidcUserService: CustomOidcUserService
    @Autowired private lateinit var customOAuth2UserService: CustomOAuth2UserService
    @Autowired private lateinit var customOAuth2SuccessHandler: CustomOAuth2SuccessHandler
    @Autowired private lateinit var customOAuth2FailureHandler: CustomOAuth2FailureHandler

    @Value("\${cors.enabled}") private val corsEnabled: Boolean = false

    @Bean
    fun filterChain(http: HttpSecurity?): SecurityFilterChain? {

        if (http != null) {
            http.csrf()?.disable()
            http.oauth2Login()?.userInfoEndpoint()?.oidcUserService(customOidcUserService)
            http.oauth2Login()?.userInfoEndpoint()?.userService(customOAuth2UserService)
            http.oauth2Login { it ->
                it.userInfoEndpoint {
                    it.oidcUserService(customOidcUserService)
                    it.userService(customOAuth2UserService)
                }
            }
            http.oauth2Login().successHandler(customOAuth2SuccessHandler)
            http.oauth2Login().failureHandler(customOAuth2FailureHandler)
            http.authorizeHttpRequests().requestMatchers(HttpMethod.POST, "/**").permitAll()
            http.authorizeHttpRequests().anyRequest().permitAll()
            if (!corsEnabled) {
                http.cors().disable()
            }
            http.authenticationProvider(authenticationProvider())
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
            //            http {
            //                csrf { disable() }
            //                oauth2Login {
            //                    userInfoEndpoint {
            //                        oidcUserService = customOidcUserService
            //                        userService = customOAuth2UserService
            //                    }
            //                    authenticationSuccessHandler = customOAuth2SuccessHandler
            //                    authenticationFailureHandler = customOAuth2FailureHandler
            //                }
            //                authorizeRequests { authorize(HttpMethod.OPTIONS, "/**", permitAll) }
            //                authenticationProvider()
            //                cors { if (!corsEnabled) disable() }
            //                sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS
            // }
            //                addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtRequestFilter)
            //            }
            return http.build()
        }
        return null
    }
    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val daoAuthenticationProvider =
            DaoAuthenticationProvider().also {
                it.setUserDetailsService(userService)
                it.setPasswordEncoder(passwordEncoder())
            }
        return daoAuthenticationProvider
    }
    @Bean
    fun authenticationManager(
        authenticationConfiguration: AuthenticationConfiguration
    ): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
    @Bean fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun requestRejectedHandler(): RequestRejectedHandler {
        return HttpStatusRequestRejectedHandler()
    }
}
