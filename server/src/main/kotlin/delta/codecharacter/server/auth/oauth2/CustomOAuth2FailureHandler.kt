package delta.codecharacter.server.auth.oauth2

import delta.codecharacter.server.exception.CustomException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@Component
class CustomOAuth2FailureHandler : AuthenticationFailureHandler {

    @Value("\${base-url}") private val baseUrl: String = ""

    private val logger: Logger = LoggerFactory.getLogger(CustomOAuth2FailureHandler::class.java)

    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        if (exception?.cause is CustomException) {
            response?.sendRedirect("$baseUrl/#/login?error=${exception.cause?.message}")
        } else {
            logger.error("Authentication failed", exception)
            response?.sendRedirect("$baseUrl/#/login?error=Internal Server Error")
        }
    }
}
