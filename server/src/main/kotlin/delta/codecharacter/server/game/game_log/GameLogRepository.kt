package delta.codecharacter.server.game.game_log

import delta.codecharacter.server.game.GameEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository interface GameLogRepository : MongoRepository<GameLogEntity, GameEntity>
