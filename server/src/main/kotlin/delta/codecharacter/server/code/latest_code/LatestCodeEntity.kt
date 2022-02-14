package delta.codecharacter.server.code.latest_code

import delta.codecharacter.server.code.LanguageEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.UUID

/**
 * Latest code entity.
 *
 * @param userId
 * @param code
 * @param language
 * @param lastSavedAt
 */
@Document(collection = "latest_code")
data class LatestCodeEntity(
    @Id val userId: UUID,
    val code: String,
    val language: LanguageEnum,
    val lastSavedAt: Instant,
)
