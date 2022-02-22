package delta.codecharacter.server.auth.reset_password

import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date
import java.util.UUID

@Document(collection = "password_reset")
data class ResetPasswordEntity(
    val userId: UUID,
    val passwordResetToken: String,
    val expiration: Date
)
