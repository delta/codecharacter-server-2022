package delta.codecharacter.server.auth.oauth2

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2ErrorCodes
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service

@Service
class CustomOidcUserService : OidcUserService() {

    private val logger: Logger = LoggerFactory.getLogger(CustomOAuth2UserService::class.java)

    override fun loadUser(userRequest: OidcUserRequest?): OidcUser {
        if (userRequest?.clientRegistration?.registrationId != "google") {
            logger.error(
                "Unsupported client registration: ${userRequest?.clientRegistration?.registrationId}"
            )
            throw OAuth2AuthenticationException(
                OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT), "Unsupported OAuth2 client"
            )
        }
        return super.loadUser(userRequest)
    }
}
