package delta.codecharacter.server.game.game_log

import delta.codecharacter.server.game.GameEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "game_log")
data class GameLogEntity(
    @Id val id: GameEntity,
    val log: String,
)
