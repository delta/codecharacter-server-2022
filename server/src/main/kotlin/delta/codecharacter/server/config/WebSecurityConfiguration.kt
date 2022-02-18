package delta.codecharacter.server.config

import delta.codecharacter.server.auth.JwtRequestFilter
import delta.codecharacter.server.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.session.SessionManagementFilter


@Configuration
@EnableWebSecurity

class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired private lateinit var corsFilter:CorsFilter
    @Autowired private lateinit var jwtRequestFilter: JwtRequestFilter
    @Autowired private lateinit var userService: UserService
    override fun configure(http: HttpSecurity?) {
        http?.cors()?.disable()
        http?.csrf()?.apply {
            disable().authorizeRequests().apply {
                antMatchers("/auth/login/**","/users").permitAll()
                antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
            }.anyRequest().authenticated()
                .and().sessionManagement().apply {
                    sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                }.and().addFilterBefore(jwtRequestFilter,UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(corsFilter,SessionManagementFilter::class.java)
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


