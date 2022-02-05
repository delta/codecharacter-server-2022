package delta.codecharacter.server.code.code_revision

import delta.codecharacter.server.user.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

/** Repository for [CodeRevisionEntity] */
@Repository
interface CodeRevisionRepository : MongoRepository<CodeRevisionEntity, UUID> {
    fun findAllByUserOrderByCreatedAtDesc(userEntity: UserEntity): List<CodeRevisionEntity>
    fun findFirstByUserOrderByCreatedAtDesc(user: UserEntity): Optional<CodeRevisionEntity>
}
