package delta.codecharacter.server.auth

import delta.codecharacter.core.AuthApi
import delta.codecharacter.dtos.PasswordLoginRequestDto
import delta.codecharacter.dtos.PasswordLoginResponseDto
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.user.UserRepository
import delta.codecharacter.server.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@Service
@CrossOrigin(origins = ["*"])
@RestController
class AuthController(
    @Autowired private val userService: UserService,
    @Autowired private val userRepository: UserRepository
) : AuthApi {
    @org.springframework.context.annotation.Lazy
    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder
    @Autowired private lateinit var authUtil: AuthUtil
    @Autowired private lateinit var authenticationManager: AuthenticationManager

    @Secured(value = ["ROLE_ANONYMOUS"])
    override fun passwordLogin(
        @RequestBody passwordLoginRequestDto: PasswordLoginRequestDto
    ): ResponseEntity<PasswordLoginResponseDto> {
        val email = passwordLoginRequestDto.email
        val password = passwordLoginRequestDto.password
        val isUser = userService.loadUserByUsername(email)
        if (verifyPassword(password, isUser.password)) {
            val jwt = authUtil.generateToken(isUser)
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(PasswordLoginResponseDto(jwt))
        } else throw CustomException(HttpStatus.NOT_FOUND, "Invalid Password")
    }

    private fun verifyPassword(request_password: String, server_password: String): Boolean {
        return passwordEncoder.matches(request_password, server_password)
    }
}
