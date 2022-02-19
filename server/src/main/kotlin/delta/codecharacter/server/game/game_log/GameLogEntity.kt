package delta.codecharacter.server.game.game_log

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document(collection = "game_log")
data class GameLogEntity(
    @Id val gameId: UUID,
    val log: String,
)
