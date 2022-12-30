package delta.codecharacter.server.daily_challenge

import delta.codecharacter.dtos.ChallengeTypeDto
import delta.codecharacter.dtos.DailyChallengeGetRequestDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document(collection = "daily_challenges")
data class DailyChallengeEntity(
        val challName: String,
        val challType: ChallengeTypeDto,
        val chall: String,
)
