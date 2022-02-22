package delta.codecharacter.server.user

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2ErrorCodes
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service

@Service
class CustomOidcUserService : OidcUserService() {
    override fun loadUser(userRequest: OidcUserRequest?): OidcUser {
        if (userRequest?.clientRegistration?.registrationId != "google") {
            throw OAuth2AuthenticationException(
                OAuth2Error(OAuth2ErrorCodes.INVALID_CLIENT), "Unsupported OAuth2 client"
            )
        }
        return super.loadUser(userRequest)
    }
}
