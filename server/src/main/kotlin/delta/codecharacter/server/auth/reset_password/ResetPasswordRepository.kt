package delta.codecharacter.server.auth.reset_password

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ResetPasswordRepository : MongoRepository<ResetPasswordEntity, String> {
    fun findFirstByPasswordResetToken(token: String): Optional<ResetPasswordEntity>
    fun deleteResetPasswordEntityByPasswordResetToken(token: String): Optional<ResetPasswordEntity>
}
