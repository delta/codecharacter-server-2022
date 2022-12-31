package delta.codecharacter.server.daily_challenge

import delta.codecharacter.dtos.ChallengeTypeDto
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "daily_challenges")
data class DailyChallengeEntity(
    val challName: String,
    val challType: ChallengeTypeDto,
    val chall: String,
)
