package delta.codecharacter.server.auth

import delta.codecharacter.dtos.AuthStatusResponseDto
import delta.codecharacter.dtos.ForgotPasswordRequestDto
import delta.codecharacter.dtos.PasswordLoginRequestDto
import delta.codecharacter.dtos.PasswordLoginResponseDto
import delta.codecharacter.dtos.ResetPasswordRequestDto
import delta.codecharacter.server.auth.reset_password.ResetPasswordEntity
import delta.codecharacter.server.auth.reset_password.ResetPasswordRepository
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.user.LoginType
import delta.codecharacter.server.user.SendGridService
import delta.codecharacter.server.user.UserEntity
import delta.codecharacter.server.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.Date
import java.util.UUID

@Service
class AuthService(
    @Autowired private val userService: UserService,
    @Autowired private val authUtil: AuthUtil,
    @Autowired private val passwordEncoder: BCryptPasswordEncoder,
    @Autowired private val sendGridService: SendGridService,
    @Autowired private val resetPasswordRepository: ResetPasswordRepository
) {
    fun passwordLogin(passwordLoginRequestDto: PasswordLoginRequestDto): PasswordLoginResponseDto {
        val email = passwordLoginRequestDto.email
        val password = passwordLoginRequestDto.password
        val user: UserEntity
        try {
            user = userService.loadUserByUsername(email)
        } catch (e: UsernameNotFoundException) {
            throw CustomException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }
        if (user.loginType != LoginType.PASSWORD) {
            throw CustomException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        }
        if (passwordEncoder.matches(password, user.password)) {
            return PasswordLoginResponseDto(authUtil.generateToken(user))
        } else throw CustomException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
    }

    fun forgotPassword(forgotPasswordRequestDto: ForgotPasswordRequestDto) {
        val email = forgotPasswordRequestDto.email
        val user =
            userService.getUserByEmail(email).orElseThrow {
                throw CustomException(HttpStatus.BAD_REQUEST, "Invalid credentials")
            }
        val passwordResetToken = UUID.randomUUID().toString()
        val passwordResetUser =
            ResetPasswordEntity(
                userId = user.id,
                passwordResetToken = passwordResetToken,
                expiration = Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)
            )
        resetPasswordRepository.save(passwordResetUser)
        sendGridService.forgotPasswordEmail(user.id, passwordResetToken, user.username, email)
    }

    fun resetPassword(resetPasswordRequestDto: ResetPasswordRequestDto) {
        val (token, password, passwordConfirmation) = resetPasswordRequestDto
        val resetPasswordEntity =
            resetPasswordRepository.findFirstByPasswordResetToken(token).orElseThrow {
                throw CustomException(HttpStatus.BAD_REQUEST, "Invalid token")
            }
        if (resetPasswordEntity.expiration > Date(System.currentTimeMillis())) {
            if (password == passwordConfirmation) {
                userService.updateUserPassword(resetPasswordEntity.userId, password)
                resetPasswordRepository.delete(resetPasswordEntity)
            } else {
                throw CustomException(
                    HttpStatus.BAD_REQUEST, "Password and Confirm Password does not match"
                )
            }
        } else {
            resetPasswordRepository.delete(resetPasswordEntity)
            throw CustomException(HttpStatus.BAD_REQUEST, "Invalid token")
        }
    }

    fun oAuth2Login(email: String, loginType: LoginType): String {
        val user = userService.getUserByEmail(email)
        val userEntity: UserEntity
        if (user.isEmpty) {
            userEntity = userService.createOAuthUser(email, loginType)
        } else {
            userEntity = user.get()
            if (userEntity.loginType != loginType) {
                throw CustomException(HttpStatus.BAD_REQUEST, "Incorrect login provider")
            }
        }
        return authUtil.generateToken(userEntity)
    }

    fun getAuthStatus(user: UserEntity): AuthStatusResponseDto {
        val status: AuthStatusResponseDto.Status =
            if (!user.isProfileComplete) {
                AuthStatusResponseDto.Status.PROFILE_INCOMPLETE
            } else if (!user.isEnabled) {
                AuthStatusResponseDto.Status.ACTIVATION_PENDING
            } else {
                AuthStatusResponseDto.Status.AUTHENTICATED
            }
        return AuthStatusResponseDto(status)
    }
}
