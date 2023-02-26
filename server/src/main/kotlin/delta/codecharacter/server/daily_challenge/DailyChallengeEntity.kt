package delta.codecharacter.server.daily_challenge

import delta.codecharacter.dtos.ChallengeTypeDto
import delta.codecharacter.dtos.DailyChallengeObjectDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document(collection = "daily_challenges")
data class DailyChallengeEntity(
    @Id val id: UUID,
    val day: Int,
    val challName: String,
    val challType: ChallengeTypeDto,
    val chall: DailyChallengeObjectDto,
    val description: String?,
    val perfectScore: Int,
    val numberOfCompletions: Int,
    val toleratedDestruction: Int,
    val map: String
)
