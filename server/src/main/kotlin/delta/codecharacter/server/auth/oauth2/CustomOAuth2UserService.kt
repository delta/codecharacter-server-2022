package delta.codecharacter.server.auth.oauth2

import delta.codecharacter.server.exception.CustomException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2ErrorCodes
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Service
class CustomOAuth2UserService : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    data class GithubEmailResponse(val email: String, val primary: Boolean, val verified: Boolean)

    private val logger: Logger = LoggerFactory.getLogger(CustomOAuth2UserService::class.java)

    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        if (userRequest?.clientRegistration?.registrationId != "github") {
            logger.error(
                "Unsupported client registration: ${userRequest?.clientRegistration?.registrationId}"
            )
            throw OAuth2AuthenticationException(
                OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT),
                "Unsupported OAuth2 client",
            )
        }
        val oAuth2User = DefaultOAuth2UserService().loadUser(userRequest)
        val attributes = mutableMapOf<String, Any>()
        attributes.putAll(oAuth2User.attributes)

        if (userRequest.accessToken?.tokenValue == null) {
            logger.error("Access token is null in GitHub authentication")
            throw OAuth2AuthenticationException(
                OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN),
                CustomException(HttpStatus.BAD_REQUEST, "Invalid token")
            )
        }

        val accessToken = userRequest.accessToken?.tokenValue
        val url = "https://api.github.com/user/emails"
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.add("Authorization", "token $accessToken")

        val primaryEmail: String?
        try {
            val json =
                restTemplate.exchange<List<GithubEmailResponse>>(
                    url, HttpMethod.GET, HttpEntity(null, headers)
                )
                    .body
            primaryEmail = json?.firstOrNull { it.primary && it.verified }?.email
        } catch (e: Exception) {
            logger.error("Failed to get primary email from github", e)
            throw OAuth2AuthenticationException(
                OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR),
                CustomException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error. Please contact event admins."
                )
            )
        }
        if (primaryEmail == null) {
            logger.error("Failed to get primary email from github for user ${oAuth2User.name}")
            throw OAuth2AuthenticationException(
                OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN),
                CustomException(HttpStatus.BAD_REQUEST, "No valid email found.")
            )
        }

        attributes["email"] = primaryEmail
        attributes["provider"] = "GITHUB"

        return DefaultOAuth2User(oAuth2User.authorities, attributes, "id")
    }
}
