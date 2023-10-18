package delta.codecharacter.server.code.locked_code

import delta.codecharacter.dtos.CodeTypeDto
import delta.codecharacter.server.code.Code
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

/**
 * Locked code entity.
 *
 * @param userId
 * @param lockedCode
 */
@Document(collection = "locked_code")
data class LockedCodeEntity(
    @Id val userId: UUID,
    val lockedCode: HashMap<CodeTypeDto, Code>,
)
