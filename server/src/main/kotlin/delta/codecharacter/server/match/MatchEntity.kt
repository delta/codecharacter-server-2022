package delta.codecharacter.server.match

import delta.codecharacter.server.game.GameEntity
import delta.codecharacter.server.user.public_user.PublicUserEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.time.Instant
import java.util.UUID

@Document(collection = "match")
data class MatchEntity(
    @Id val id: UUID,
    val games: Set<GameEntity>,
    val mode: MatchModeEnum,
    val verdict: MatchVerdictEnum,
    val createdAt: Instant,
    val totalPoints: Int,
    @DocumentReference(lazy = true) val player1: PublicUserEntity,
    @DocumentReference(lazy = true) val player2: PublicUserEntity,
)
