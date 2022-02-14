package delta.codecharacter.server.game.queue.entities

import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.server.game.GameStatusEnum
import java.util.UUID

data class GameStatusUpdateEntity(
    @field:JsonProperty("game_id", required = true) val gameId: UUID,
    @field:JsonProperty("game_status", required = true) val gameStatus: GameStatusEnum,
    @field:JsonProperty("game_result", required = false) val gameResult: GameResultEntity?
)
