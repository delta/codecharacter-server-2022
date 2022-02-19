package delta.codecharacter.server.auth

import delta.codecharacter.dtos.PasswordLoginRequestDto
import delta.codecharacter.dtos.PasswordLoginResponseDto
import delta.codecharacter.server.exception.CustomException
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
    @Autowired private val authUtil: AuthUtil,
    @Autowired private val passwordEncoder: BCryptPasswordEncoder
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
}
