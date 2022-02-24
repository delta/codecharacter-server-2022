package delta.codecharacter.server.auth

import delta.codecharacter.dtos.AuthStatusResponseDto
import delta.codecharacter.dtos.ForgotPasswordRequestDto
import delta.codecharacter.dtos.PasswordLoginRequestDto
import delta.codecharacter.dtos.PasswordLoginResponseDto
import delta.codecharacter.dtos.ResetPasswordRequestDto
import delta.codecharacter.server.auth.jwt.JwtService
import delta.codecharacter.server.auth.reset_password.ResetPasswordService
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.user.LoginType
import delta.codecharacter.server.user.UserEntity
import delta.codecharacter.server.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    @Autowired private val userService: UserService,
    @Autowired private val jwtService: JwtService,
    @Autowired private val resetPasswordService: ResetPasswordService,
    @Autowired private val passwordEncoder: BCryptPasswordEncoder,
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
            return PasswordLoginResponseDto(jwtService.generateToken(user))
        } else throw CustomException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
    }

    fun forgotPassword(forgotPasswordRequestDto: ForgotPasswordRequestDto) {
        val email = forgotPasswordRequestDto.email
        val user =
            userService.getUserByEmail(email).orElseThrow {
                throw CustomException(HttpStatus.BAD_REQUEST, "Invalid credentials")
            }
        if (user.loginType != LoginType.PASSWORD) {
            throw CustomException(HttpStatus.BAD_REQUEST, "User not registered with password")
        }
        resetPasswordService.sendResetPasswordEmail(user)
    }

    fun resetPassword(resetPasswordRequestDto: ResetPasswordRequestDto) {
        val (token, password, passwordConfirmation) = resetPasswordRequestDto
        if (password != passwordConfirmation) {
            throw CustomException(
                HttpStatus.BAD_REQUEST,
                "Password and Confirm Password does not match"
            )
        }
        val userId = resetPasswordService.processResetPasswordTokenAndGetUserId(token)
        userService.updateUserPassword(userId, password)
    }

    fun oAuth2Login(email: String, loginType: LoginType): String {
        val user = userService.getUserByEmail(email)
        val userEntity: UserEntity
        if (user.isEmpty) {
            userEntity = userService.createUserWithOAuth(email, loginType)
        } else {
            userEntity = user.get()
            if (userEntity.loginType != loginType) {
                throw CustomException(HttpStatus.BAD_REQUEST, "Incorrect login provider")
            }
        }
        return jwtService.generateToken(userEntity)
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
