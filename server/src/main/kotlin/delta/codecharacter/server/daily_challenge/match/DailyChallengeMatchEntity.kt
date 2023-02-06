package delta.codecharacter.server.daily_challenge.match

import delta.codecharacter.server.game.GameEntity
import delta.codecharacter.server.user.public_user.PublicUserEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.DocumentReference
import java.time.Instant
import java.util.UUID

@Document(collation = "daily_challenge_match")
data class DailyChallengeMatchEntity(
    @Id val id: UUID,
    @DocumentReference(lazy = true) val game: GameEntity,
    val verdict: DailyChallengeMatchVerdictEnum,
    @DocumentReference(lazy = true) val user: PublicUserEntity,
    val createdAt: Instant
)
