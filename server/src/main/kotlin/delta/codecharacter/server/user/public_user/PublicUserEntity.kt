package delta.codecharacter.server.user.public_user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document(collection = "public_user")
data class PublicUserEntity(
    @Id val userId: UUID,
    val username: String,
    val name: String,
    val country: String,
    val college: String,
    val avatarId: Int,
    val rating: Double,
    val wins: Int,
    val losses: Int,
    val ties: Int,
)
