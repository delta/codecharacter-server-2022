package delta.codecharacter.server.game

import delta.codecharacter.server.daily_challenge.DailyChallenge
import delta.codecharacter.server.daily_challenge.DailyChallengeType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date
import java.util.UUID

@Document(collection = "daily_challenges") // change it later
data class DailyChallengeEntity(
        @Id val id: UUID,
        val name: String,
        val type: DailyChallengeType,
        val description: String,
        val date: Date,
        val challenge: DailyChallenge
)
