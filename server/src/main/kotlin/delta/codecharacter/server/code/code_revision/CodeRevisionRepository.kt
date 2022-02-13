package delta.codecharacter.server.code.code_revision

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

/** Repository for [CodeRevisionEntity] */
@Repository
interface CodeRevisionRepository : MongoRepository<CodeRevisionEntity, UUID> {
    fun findAllByUserIdOrderByCreatedAtDesc(userId: UUID): List<CodeRevisionEntity>
    fun findFirstByUserIdOrderByCreatedAtDesc(userId: UUID): Optional<CodeRevisionEntity>
}
