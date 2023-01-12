package delta.codecharacter.server.daily_challenge

import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.dtos.ChallengeTypeDto

data class DailyChallengeObject(val day: Int,
    val challName: String,
    val challType: ChallengeTypeDto,
    val chall: String,
    val description: String?,
)
