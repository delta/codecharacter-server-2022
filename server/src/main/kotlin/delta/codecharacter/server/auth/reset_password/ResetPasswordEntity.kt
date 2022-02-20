package delta.codecharacter.server.auth.reset_password

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date
import java.util.UUID

@Document(collection = "PasswordReset")
data class ResetPasswordEntity(
    @Id val id: UUID,
    val userId: UUID,
    val passwordResetToken: String,
    val expiration: Date
)
