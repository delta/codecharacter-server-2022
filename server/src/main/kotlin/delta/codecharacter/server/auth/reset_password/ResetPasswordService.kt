package delta.codecharacter.server.auth.reset_password

import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.sendgrid.SendGridService
import delta.codecharacter.server.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.Date
import java.util.UUID

@Service
class ResetPasswordService(
    @Autowired private val resetPasswordRepository: ResetPasswordRepository,
    @Autowired private val sendGridService: SendGridService
) {
    fun sendResetPasswordEmail(user: UserEntity) {
        val passwordResetToken = UUID.randomUUID().toString()
        val passwordResetUser =
            ResetPasswordEntity(
                userId = user.id,
                passwordResetToken = passwordResetToken,
                expiration = Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)
            )
        resetPasswordRepository.save(passwordResetUser)
        sendGridService.resetPasswordEmail(user.id, passwordResetToken, user.username, user.email)
    }

    fun processResetPasswordTokenAndGetUserId(token: String): UUID {
        val resetPasswordEntity =
            resetPasswordRepository.findFirstByPasswordResetToken(token).orElseThrow {
                throw CustomException(HttpStatus.BAD_REQUEST, "Invalid token")
            }
        resetPasswordRepository.deleteResetPasswordEntityByPasswordResetToken(token)
        if (resetPasswordEntity.expiration < Date(System.currentTimeMillis())) {
            resetPasswordRepository.deleteResetPasswordEntityByPasswordResetToken(token)
            throw CustomException(HttpStatus.BAD_REQUEST, "Token expired")
        }
        return resetPasswordEntity.userId
    }
}
