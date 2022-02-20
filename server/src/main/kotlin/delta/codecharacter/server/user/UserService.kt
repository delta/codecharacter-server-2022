package delta.codecharacter.server.user

import delta.codecharacter.dtos.RegisterUserRequestDto
import delta.codecharacter.dtos.UpdatePasswordRequestDto
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.user.activate_user.ActivateUserRepository
import delta.codecharacter.server.user.activate_user.ActivationuserEntity
import delta.codecharacter.server.user.public_user.PublicUserService
import delta.codecharacter.server.user.rating_history.RatingHistoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.Date
import java.util.UUID

@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val publicUserService: PublicUserService,
    @Autowired private val ratingHistoryService: RatingHistoryService,
    @Autowired private val activateUserRepository: ActivateUserRepository,
    @Autowired private val sendGridService: SendGridService
) : UserDetailsService {

    @Lazy @Autowired private lateinit var passwordEncoder: BCryptPasswordEncoder

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

    private fun createUser(userId: UUID, username: String, password: String, email: String) {
        val user =
            UserEntity(
                id = userId,
                username = username,
                password = passwordEncoder.encode(password),
                email = email,
                isEnabled = true,
                isAccountNonExpired = true,
                isAccountNonLocked = true,
                isCredentialsNonExpired = true,
            )
        userRepository.save(user)
        createActivationuserTable(userId, UUID.randomUUID(), username, email)
    }

    fun getUserById(id: UUID): UserEntity {
        val user = userRepository.findById(id)
        if (user.isEmpty || !user.get().isEnabled || !user.get().isAccountNonExpired) {
            throw CustomException(HttpStatus.BAD_REQUEST, "User not found")
        } else {
            return user.get()
        }
    }

    fun verifyUserPassword(id: UUID, password: String): Boolean {
        val user = userRepository.findById(id)
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
        val (username, name, email, password, passwordConfirmation, country, college, avatarId) =
            registerUserRequestDto

        if (password != passwordConfirmation) {
            throw CustomException(
                HttpStatus.BAD_REQUEST, "Password and password confirmation don't match"
            )
        }

        val userId = UUID.randomUUID()
        try {
            createUser(userId, username, password, email)
            publicUserService.create(userId, username, name, country, college, avatarId)
            ratingHistoryService.create(userId)
        } catch (duplicateError: DuplicateKeyException) {
            throw CustomException(HttpStatus.BAD_REQUEST, "Username/Email already exists")
        }
    }
    fun createActivationuserTable(userId: UUID, id: UUID, name: String, email: String) {
        val token = UUID.randomUUID().toString()
        val expirationTime = Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)
        val activationUser =
            ActivationuserEntity(
                id = id,
                userId = userId,
                token = token,
                expiration = expirationTime,
            )
        activateUserRepository.save(activationUser)
        val user = activateUserRepository.findFirstByUserId(userId)

        if (!user.isEmpty) sendGridService.activateUserEmail(userId, token, name, email)
    }
    fun activateUser(userId: UUID, token: String) {
        val unactivatedUserInfo: ActivationuserEntity
        val unactivatedUser = activateUserRepository.findFirstByToken(token)
        if (unactivatedUser.isEmpty) throw UsernameNotFoundException("User is not yet registered")
        else unactivatedUserInfo = unactivatedUser.get()
        if (unactivatedUserInfo.expiration > Date(System.currentTimeMillis())) {
            val user = userRepository.findFirstById((userId)).get()
            val activatedUser = user.copy(isEnabled = true)
            userRepository.save(activatedUser)
            activateUserRepository.delete(unactivatedUser.get())
        } else {
            val user = userRepository.findFirstById((userId))
            if (!user.isEmpty) userRepository.delete(user.get())
            activateUserRepository.delete(unactivatedUser.get())
            throw CustomException(HttpStatus.SERVICE_UNAVAILABLE, "Service Timeout!!")
        }
    }
}
