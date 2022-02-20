package delta.codecharacter.server.auth.reset_password

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface ResetPasswordRepository : MongoRepository<ResetPasswordEntity, UUID> {
    fun findFirstByPasswordResetToken(token: String): Optional<ResetPasswordEntity>
    fun findFirstByUserId(userId: UUID): Optional<ResetPasswordEntity>
}
