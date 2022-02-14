package delta.codecharacter.server.code.code_revision

import delta.codecharacter.server.code.LanguageEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.time.Instant
import java.util.UUID

/**
 * Code Revision Entity
 *
 * @param id
 * @param code
 * @param language
 * @param parentRevision
 * @param userId
 * @param createdAt
 */
@Document(collection = "code_revision")
data class CodeRevisionEntity(
    @Id val id: UUID,
    val code: String,
    val message: String,
    val language: LanguageEnum,
    @DocumentReference(lazy = true) val parentRevision: CodeRevisionEntity?,
    val userId: UUID,
    val createdAt: Instant
)
