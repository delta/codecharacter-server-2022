package delta.codecharacter.server.auth

import delta.codecharacter.dtos.ForgotPasswordRequestDto
import delta.codecharacter.dtos.PasswordLoginRequestDto
import delta.codecharacter.dtos.PasswordLoginResponseDto
import delta.codecharacter.dtos.ResetPasswordRequestDto
import delta.codecharacter.server.auth.reset_password.ResetPasswordEntity
import delta.codecharacter.server.auth.reset_password.ResetPasswordRepository
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.user.SendGridService
import delta.codecharacter.server.user.UserEntity
import delta.codecharacter.server.user.UserRepository
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
    @Autowired private val userRepository: UserRepository,
    @Autowired private val resetPasswordRepository: ResetPasswordRepository
) {
    fun passwordLogin(passwordLoginRequestDto: PasswordLoginRequestDto): PasswordLoginResponseDto {
        val email = passwordLoginRequestDto.email
        val password = passwordLoginRequestDto.password
        val user: UserEntity
        try {
            user = userService.loadUserByUsername(email)
        } catch (e: UsernameNotFoundException) {
            throw CustomException(HttpStatus.UNAUTHORIZED, "Invalid email or password")
        }
        if (passwordEncoder.matches(password, user.password)) {
            return PasswordLoginResponseDto(authUtil.generateToken(user))
        } else throw CustomException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
    }

    fun forgotPassword(forgotPasswordRequestDto: ForgotPasswordRequestDto) {
        val user = userRepository.findFirstByEmail(email = forgotPasswordRequestDto.email)
        if (!user.isEmpty) {
            val passwordResetToken = UUID.randomUUID().toString()
            val passwordResetUser =
                ResetPasswordEntity(
                    id = UUID.randomUUID(),
                    userId = user.get().id,
                    passwordResetToken = passwordResetToken,
                    expiration = Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)
                )
            resetPasswordRepository.save(passwordResetUser)
            sendGridService.forgotPasswordEmail(
                user.get().id, passwordResetToken, user.get().username, forgotPasswordRequestDto.email
            )
        } else {
            throw CustomException(HttpStatus.BAD_REQUEST, "Invalid request")
        }
    }

    fun resetPassword(resetPasswordRequestDto: ResetPasswordRequestDto) {
        val resetPasswordUser =
            resetPasswordRepository.findFirstByPasswordResetToken(resetPasswordRequestDto.token)
        if (!resetPasswordUser.isEmpty) {
            if (resetPasswordUser.get().expiration > Date(System.currentTimeMillis())) {
                if (resetPasswordRequestDto.password == resetPasswordRequestDto.passwordConfirmation) {
                    val user = userRepository.findFirstById(resetPasswordUser.get().userId).get()
                    val resetUserPassword =
                        user.copy(
                            password = passwordEncoder.encode(resetPasswordRequestDto.passwordConfirmation)
                        )
                    userRepository.save(resetUserPassword)
                    resetPasswordRepository.delete(resetPasswordUser.get())
                } else {
                    throw CustomException(
                        HttpStatus.BAD_REQUEST, "Password and Confirm Password does not match"
                    )
                }
            } else {
                resetPasswordRepository.delete(resetPasswordUser.get())
                throw CustomException(HttpStatus.REQUEST_TIMEOUT, "Reset Password Request timeout")
            }
        } else {
            throw Exception("Request Not found , Try again!")
        }
    }
}
