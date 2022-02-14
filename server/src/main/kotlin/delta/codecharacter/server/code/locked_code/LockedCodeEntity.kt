package delta.codecharacter.server.code.locked_code

import delta.codecharacter.server.code.LanguageEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

/**
 * Locked code entity.
 *
 * @param userId
 * @param code
 * @param language
 */
@Document(collection = "locked_code")
data class LockedCodeEntity(@Id val userId: UUID, val code: String, val language: LanguageEnum)
