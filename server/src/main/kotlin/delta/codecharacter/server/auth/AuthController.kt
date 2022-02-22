package delta.codecharacter.server.auth

import delta.codecharacter.core.AuthApi
import delta.codecharacter.dtos.ForgotPasswordRequestDto
import delta.codecharacter.dtos.PasswordLoginRequestDto
import delta.codecharacter.dtos.PasswordLoginResponseDto
import delta.codecharacter.dtos.ResetPasswordRequestDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
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

    @Secured(value = ["ROLE_ANONYMOUS"])
    override fun forgotPassword(
        forgotPasswordRequestDto: ForgotPasswordRequestDto
    ): ResponseEntity<Unit> {
        authService.forgotPassword(forgotPasswordRequestDto)
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Unit)
    }

    @Secured(value = ["ROLE_ANONYMOUS"])
    override fun resetPassword(
        resetPasswordRequestDto: ResetPasswordRequestDto
    ): ResponseEntity<Unit> {
        authService.resetPassword(resetPasswordRequestDto)
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Unit)
    }
}
