package delta.codecharacter.server.user.activate_user

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface ActivateUserRepository : MongoRepository<ActivationuserEntity, UUID> {
    fun findFirstByUserId(userId: UUID): Optional<ActivationuserEntity>
    fun findFirstByToken(token: String): Optional<ActivationuserEntity>
}
