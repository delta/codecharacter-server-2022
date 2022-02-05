package delta.codecharacter.server.code.code_revision

import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.user.UserEntity
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
 * @param user
 * @param createdAt
 */
@Document(collection = "code_revision")
data class CodeRevisionEntity(
    @Id val id: UUID,
    val code: String,
    val language: LanguageEnum,
    @DocumentReference(lazy = true) val parentRevision: CodeRevisionEntity?,
    @DocumentReference(lazy = true) val user: UserEntity,
    val createdAt: Instant
)
