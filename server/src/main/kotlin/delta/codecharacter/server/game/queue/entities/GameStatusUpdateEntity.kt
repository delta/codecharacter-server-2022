package delta.codecharacter.server.game.queue.entities

import delta.codecharacter.server.game.GameStatusEnum
import java.util.UUID

data class GameStatusUpdateEntity(
    val gameId: UUID,
    val gameStatus: GameStatusEnum,
    val gameResult: GameResultEntity?
)
