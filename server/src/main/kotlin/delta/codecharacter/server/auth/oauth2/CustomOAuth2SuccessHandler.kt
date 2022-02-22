package delta.codecharacter.server.auth.oauth2

import delta.codecharacter.server.auth.AuthService
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.user.LoginType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CustomOAuth2SuccessHandler(@Lazy @Autowired private val authService: AuthService) :
    AuthenticationSuccessHandler {

    @Value("\${base-url}") private val baseUrl: String = ""

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
                response?.addCookie(Cookie("bearer-token", token))
                if (baseUrl.contains("https")) {
                    response?.setHeader(
                        "Set-Cookie", "bearer-token=$token; Path=/; HttpOnly; Secure; SameSite=None"
                    )
                } else {
                    response?.setHeader("Set-Cookie", "bearer-token=$token; Path=/; HttpOnly; SameSite=false")
                }
                response?.sendRedirect("$baseUrl/#/dashboard")
            } catch (e: CustomException) {
                response?.sendRedirect("$baseUrl/#/login?error=${e.message}")
            }
        } else {
            response?.sendRedirect("$baseUrl/#/login?error=Invalid login, please try again")
        }
    }
}
