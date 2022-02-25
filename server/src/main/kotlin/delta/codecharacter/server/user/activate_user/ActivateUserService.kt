package delta.codecharacter.server.user.activate_user

import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.sendgrid.SendGridService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.Date
import java.util.UUID

@Service
class ActivateUserService(
    @Autowired private val activateUserRepository: ActivateUserRepository,
    @Autowired private val sendGridService: SendGridService
) {

    fun sendActivationToken(userId: UUID, name: String, email: String) {
        val token = UUID.randomUUID().toString()
        val expirationTime = Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)
        val activateUserEntity =
            ActivateUserEntity(
                id = UUID.randomUUID(),
                userId = userId,
                token = token,
                expiration = expirationTime,
            )
        activateUserRepository.save(activateUserEntity)
        val user = activateUserRepository.findFirstByUserId(userId)

        if (!user.isEmpty) sendGridService.activateUserEmail(userId, token, name, email)
    }

    fun processActivationToken(userId: UUID, token: String) {
        val unactivatedUser =
            activateUserRepository.findFirstByToken(token).orElseThrow {
                CustomException(HttpStatus.BAD_REQUEST, "Invalid token")
            }
        activateUserRepository.delete(unactivatedUser)
        if (unactivatedUser.expiration < Date(System.currentTimeMillis())) {
            throw CustomException(HttpStatus.BAD_REQUEST, "Token expired")
        }
    }
}
