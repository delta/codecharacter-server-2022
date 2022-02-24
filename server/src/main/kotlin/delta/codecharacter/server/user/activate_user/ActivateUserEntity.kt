package delta.codecharacter.server.user.activate_user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date
import java.util.UUID

@Document(collection = "activation_user")
data class ActivateUserEntity(
    @Id val id: UUID,
    val userId: UUID,
    val token: String,
    val expiration: Date
)
