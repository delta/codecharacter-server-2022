package delta.codecharacter.server.user

import delta.codecharacter.dtos.CompleteProfileRequestDto
import delta.codecharacter.dtos.RegisterUserRequestDto
import delta.codecharacter.dtos.UpdatePasswordRequestDto
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.user.activate_user.ActivateUserService
import delta.codecharacter.server.user.public_user.PublicUserService
import delta.codecharacter.server.user.rating_history.RatingHistoryService
import org.bson.json.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.Optional
import java.util.UUID

@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val publicUserService: PublicUserService,
    @Autowired private val ratingHistoryService: RatingHistoryService,
    @Autowired private val activateUserService: ActivateUserService
) : UserDetailsService {

    @Lazy @Autowired private lateinit var passwordEncoder: BCryptPasswordEncoder
    @Value("\${environment.reCaptcha-key}") private lateinit var secretKey: String

    override fun loadUserByUsername(email: String?): UserEntity {
        if (email == null) {
            throw UsernameNotFoundException("User not found")
        }
        val user = userRepository.findFirstByEmail(email)
        if (user.isEmpty) {
            throw UsernameNotFoundException("User not found")
        } else if (!user.get().isEnabled) {
            throw CustomException(HttpStatus.UNAUTHORIZED, "Email not verified")
        } else if (!user.get().isAccountNonExpired) {
            throw CustomException(HttpStatus.UNAUTHORIZED, "Account expired")
        } else {
            return user.get()
        }
    }

    private fun createUserWithPassword(userId: UUID, password: String, email: String) {
        val user =
            UserEntity(
                id = userId,
                password = passwordEncoder.encode(password),
                email = email,
                loginType = LoginType.PASSWORD,
                isProfileComplete = true,
                isEnabled = false,
                isAccountNonExpired = true,
                isAccountNonLocked = true,
                isCredentialsNonExpired = true,
                userAuthorities = mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
            )
        userRepository.save(user)
    }

    fun createUserWithOAuth(email: String, oauthProvider: LoginType): UserEntity {
        val userId = UUID.randomUUID()
        val user =
            UserEntity(
                id = userId,
                password = passwordEncoder.encode(UUID.randomUUID().toString()),
                email = email,
                loginType = oauthProvider,
                isProfileComplete = false,
                isEnabled = true,
                isAccountNonExpired = true,
                isAccountNonLocked = true,
                isCredentialsNonExpired = true,
                userAuthorities = mutableListOf(SimpleGrantedAuthority("ROLE_USER_INCOMPLETE_PROFILE"))
            )
        ratingHistoryService.create(userId)
        return userRepository.save(user)
    }

    fun getUserByEmail(email: String): Optional<UserEntity> {
        return userRepository.findFirstByEmail(email)
    }

    fun updateUserPassword(userId: UUID, password: String) {
        val user = userRepository.findById(userId).get()
        userRepository.save(user.copy(password = passwordEncoder.encode(password)))
    }

    fun verifyUserPassword(userId: UUID, password: String): Boolean {
        val user = userRepository.findById(userId)
        return passwordEncoder.matches(password, user.get().password)
    }

    fun updatePassword(userId: UUID, updatePasswordRequestDto: UpdatePasswordRequestDto) {
        val (oldPassword, password, passwordConfirmation) = updatePasswordRequestDto
        if (password != passwordConfirmation) {
            throw CustomException(HttpStatus.BAD_REQUEST, "Passwords do not match")
        }
        if (verifyUserPassword(userId, oldPassword)) {
            val user = userRepository.findById(userId).get()
            userRepository.save(user.copy(password = passwordEncoder.encode(password)))
        } else {
            throw CustomException(HttpStatus.BAD_REQUEST, "Old password is incorrect")
        }
    }

    fun registerUser(registerUserRequestDto: RegisterUserRequestDto) {
        val (
            username,
            name,
            email,
            password,
            passwordConfirmation,
            country,
            college,
            avatarId,
            recaptchaCode
        ) =
            registerUserRequestDto

        if (username.trim().length < 5) {
            throw CustomException(HttpStatus.BAD_REQUEST, "Username must be minimum 5 characters long")
        }
        if (name.trim().length < 5) {
            throw CustomException(HttpStatus.BAD_REQUEST, "Name must be minimum 5 characters long")
        }
        if (avatarId !in 0..19) {
            throw CustomException(HttpStatus.BAD_REQUEST, "Selected Avatar is invalid")
        }
        if (college.trim().isEmpty()) {
            throw CustomException(HttpStatus.BAD_REQUEST, "College can not be empty")
        }
        if (country.trim().isEmpty()) {
            throw CustomException(HttpStatus.BAD_REQUEST, "Country can not be empty")
        }
        if (password != passwordConfirmation) {
            throw CustomException(
                HttpStatus.BAD_REQUEST, "Password and password confirmation don't match"
            )
        }

        if (!publicUserService.isUsernameUnique(username)) {
            throw CustomException(HttpStatus.BAD_REQUEST, "Username already taken")
        }

        if (!verifyReCaptcha(recaptchaCode))
            throw CustomException(HttpStatus.BAD_REQUEST, "Invalid ReCaptcha")

        val userId = UUID.randomUUID()
        try {
            createUserWithPassword(userId, password, email)
            publicUserService.create(userId, username, name, country, college, avatarId)
            ratingHistoryService.create(userId)
            activateUserService.sendActivationToken(userId, name, email)
        } catch (duplicateError: DuplicateKeyException) {
            throw CustomException(HttpStatus.BAD_REQUEST, "Username/Email already exists")
        }
    }

    fun verifyReCaptcha(reCaptchaResponse: String): Boolean {
        val url =
            "https://www.google.com/recaptcha/api/siteverify?secret=$secretKey&response=$reCaptchaResponse"
        try {
            val client = HttpClient.newBuilder().build()
            val request =
                HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            val json = JsonObject(response.body()).toBsonDocument()
            return (
                json.getBoolean("success").value &&
                    (json.getDouble("score").value.compareTo(0.5) >= 0)
                )
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun activateUser(userId: UUID, token: String) {
        activateUserService.processActivationToken(userId, token)
        val user = userRepository.findFirstById((userId)).get()
        val activatedUser = user.copy(isEnabled = true)
        userRepository.save(activatedUser)
    }

    fun completeUserProfile(userId: UUID, completeProfileRequestDto: CompleteProfileRequestDto) {
        val (username, name, country, college, avatarId) = completeProfileRequestDto
        val user = userRepository.findFirstById(userId).get()
        publicUserService.create(userId, username, name, country, college, avatarId)
        userRepository.save(
            user.copy(
                isProfileComplete = true,
                userAuthorities = mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
            )
        )
    }
}
