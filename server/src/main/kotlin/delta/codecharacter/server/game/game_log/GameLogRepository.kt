package delta.codecharacter.server.game.game_log

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository interface GameLogRepository : MongoRepository<GameLogEntity, UUID>
