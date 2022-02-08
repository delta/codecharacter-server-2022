package delta.codecharacter.server.user

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : MongoRepository<UserEntity, String> {
    fun findFirstByEmail(email: String): Optional<UserEntity>
}
