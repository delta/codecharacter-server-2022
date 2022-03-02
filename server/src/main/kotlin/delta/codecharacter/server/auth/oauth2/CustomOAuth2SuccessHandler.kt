package delta.codecharacter.server.auth.oauth2

import delta.codecharacter.server.auth.AuthService
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.user.LoginType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CustomOAuth2SuccessHandler(@Lazy @Autowired private val authService: AuthService) :
    AuthenticationSuccessHandler {

    @Value("\${base-url}") private val baseUrl: String = ""
    @Value("\${frontend-domain}") private val frontendDomain: String = ""

    private val logger: Logger = LoggerFactory.getLogger(CustomOAuth2SuccessHandler::class.java)

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val principal = authentication?.principal
        if (principal is OAuth2AuthenticatedPrincipal) {
            val attributes = principal.attributes
            val email = attributes["email"] as String
            val provider = attributes["provider"] as String?
            val loginType = LoginType.valueOf(provider ?: "GOOGLE")
            try {
                val token = authService.oAuth2Login(email, loginType)
                val instant = Instant.now()
                val zoneId = ZoneId.of("GMT")
                val zonedDateTimePlus = ZonedDateTime.ofInstant(instant.plusSeconds(60), zoneId)
                val plusOneSecond = zonedDateTimePlus.format(DateTimeFormatter.RFC_1123_DATE_TIME)
                if (baseUrl.contains("https")) {
                    response?.setHeader(
                        "Set-Cookie",
                        "bearer-token=$token; Domain=$frontendDomain; Path=/;Expires=$plusOneSecond; Secure; SameSite=None"
                    )
                } else {
                    response?.setHeader(
                        "Set-Cookie",
                        "bearer-token=$token; Domain=$frontendDomain;Expires=$plusOneSecond; Path=/; SameSite=false"
                    )
                }
                response?.sendRedirect("$baseUrl/#/dashboard")
            } catch (e: CustomException) {
                response?.sendRedirect("$baseUrl/#/login?error=${e.message}")
            } catch (e: Exception) {
                logger.error("Unexpected error", e)
                response?.sendRedirect("$baseUrl/#/login?error=Internal Server Error")
            }
        } else {
            response?.sendRedirect("$baseUrl/#/login?error=Invalid login, please try again")
        }
    }
}
