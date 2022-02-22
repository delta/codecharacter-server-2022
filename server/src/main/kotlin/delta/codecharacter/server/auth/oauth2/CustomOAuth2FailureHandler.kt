package delta.codecharacter.server.auth.oauth2

import delta.codecharacter.server.exception.CustomException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CustomOAuth2FailureHandler : AuthenticationFailureHandler {

    @Value("\${base-url}") private val baseUrl: String = ""

    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        if (exception?.cause is CustomException) {
            response?.sendRedirect("$baseUrl/#/login?error=${exception.cause?.message}")
        } else {
            response?.sendRedirect("$baseUrl/#/login?error=Internal Server Error")
        }
    }
}
