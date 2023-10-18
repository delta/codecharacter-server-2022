package delta.codecharacter.server.code.latest_code

import delta.codecharacter.dtos.CodeTypeDto
import delta.codecharacter.server.code.Code
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

/**
 * Latest code entity.
 *
 * @param userId
 * @param latestCode
 */
@Document(collection = "latest_code")
data class LatestCodeEntity(
    @Id val userId: UUID,
    val latestCode: HashMap<CodeTypeDto, Code>,
)
