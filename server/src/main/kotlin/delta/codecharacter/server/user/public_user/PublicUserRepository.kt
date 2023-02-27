package delta.codecharacter.server.user.public_user

import delta.codecharacter.dtos.TierTypeDto
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional
import java.util.UUID

interface PublicUserRepository : MongoRepository<PublicUserEntity, UUID> {
    fun findByUsername(username: String): Optional<PublicUserEntity>

    fun findAllByTier(tier: TierTypeDto?, pageRequest: PageRequest): List<PublicUserEntity>
}
