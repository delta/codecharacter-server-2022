package delta.codecharacter.server.user

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface UserRepository : MongoRepository<UserEntity, UUID> {
    fun findFirstByEmail(email: String): Optional<UserEntity>
}
