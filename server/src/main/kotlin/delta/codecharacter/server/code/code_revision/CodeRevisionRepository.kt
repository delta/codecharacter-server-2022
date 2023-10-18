package delta.codecharacter.server.code.code_revision

import delta.codecharacter.dtos.CodeTypeDto
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

/** Repository for [CodeRevisionEntity] */
@Repository
interface CodeRevisionRepository : MongoRepository<CodeRevisionEntity, UUID> {
    fun findAllByUserIdAndCodeTypeOrderByCreatedAtDesc(
        userId: UUID,
        codeType: CodeTypeDto
    ): List<CodeRevisionEntity>
    fun findFirstByUserIdAndCodeTypeOrderByCreatedAtDesc(
        userId: UUID,
        codeType: CodeTypeDto
    ): Optional<CodeRevisionEntity>
}
