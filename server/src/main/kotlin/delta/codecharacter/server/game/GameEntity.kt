package delta.codecharacter.server.game

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document(collection = "game")
data class GameEntity(
    @Id val id: UUID,
    val coinsUsed: Int,
    val destruction: Float,
    val status: GameStatusEnum,
    val verdict: GameVerdictEnum,
    val matchId: UUID
)
