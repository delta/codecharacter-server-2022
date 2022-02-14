package delta.codecharacter.server.user.public_user

import org.springframework.data.mongodb.repository.MongoRepository
import java.util.UUID

interface PublicUserRepository : MongoRepository<PublicUserEntity, UUID>
