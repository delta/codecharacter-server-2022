package delta.codecharacter.server.code.latest_code

import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.user.UserEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

/**
 * Latest code entity.
 *
 * @param user
 * @param code
 * @param language
 * @param lastSavedAt
 */
@Document(collection = "latest_code")
data class LatestCodeEntity(
    @Id val user: UserEntity,
    val code: String,
    val language: LanguageEnum,
    val lastSavedAt: Instant,
)
