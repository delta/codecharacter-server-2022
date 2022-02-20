package delta.codecharacter.server.user.activate_user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date
import java.util.UUID

@Document(collection = "activationUser")
data class ActivationuserEntity(
    @Id val id: UUID,
    val userId: UUID,
    val token: String,
    val expiration: Date
)
