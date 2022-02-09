package delta.codecharacter.server.game_map.map_revision

import delta.codecharacter.server.user.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

/** Repository for [MapRevisionEntity] */
@Repository
interface MapRevisionRepository : MongoRepository<MapRevisionEntity, UUID> {
    fun findAllByUserOrderByCreatedAtDesc(userEntity: UserEntity): List<MapRevisionEntity>
    fun findFirstByUserOrderByCreatedAtDesc(user: UserEntity): Optional<MapRevisionEntity>
}
