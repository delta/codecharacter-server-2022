package delta.codecharacter.server.game

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository interface GameRepository : MongoRepository<GameEntity, UUID>
