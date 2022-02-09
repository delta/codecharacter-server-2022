package delta.codecharacter.server.game.queue.entities

import java.util.UUID

data class GameResultEntity(
    val gameId: UUID,
    val destructionPercentage: Float,
    val coinsRemaining: Int,
    val hasErrors: Boolean,
    val log: String,
)
