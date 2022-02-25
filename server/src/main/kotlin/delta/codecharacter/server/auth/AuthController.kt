package delta.codecharacter.server.auth

import delta.codecharacter.core.AuthApi
import delta.codecharacter.dtos.AuthStatusResponseDto
import delta.codecharacter.dtos.ForgotPasswordRequestDto
import delta.codecharacter.dtos.PasswordLoginRequestDto
import delta.codecharacter.dtos.PasswordLoginResponseDto
import delta.codecharacter.dtos.ResetPasswordRequestDto
import delta.codecharacter.server.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
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

    @Secured(value = ["ROLE_USER", "ROLE_USER_INCOMPLETE_PROFILE"])
    override fun getAuthStatus(): ResponseEntity<AuthStatusResponseDto> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        return ResponseEntity.ok(authService.getAuthStatus(user))
    }
}
