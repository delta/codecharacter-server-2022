package delta.codecharacter.server.auth

import delta.codecharacter.core.AuthApi
import delta.codecharacter.dtos.PasswordLoginRequestDto
import delta.codecharacter.dtos.PasswordLoginResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(@Autowired private val authService: AuthService) : AuthApi {

    @Secured(value = ["ROLE_ANONYMOUS"])
    override fun passwordLogin(
        @RequestBody passwordLoginRequestDto: PasswordLoginRequestDto
    ): ResponseEntity<PasswordLoginResponseDto> {
        return ResponseEntity.ok(authService.passwordLogin(passwordLoginRequestDto))
    }
}
