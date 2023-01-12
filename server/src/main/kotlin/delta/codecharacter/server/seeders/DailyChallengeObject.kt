package delta.codecharacter.server.seeders

import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.dtos.ChallengeTypeDto

data class DailyChallengeObject(
    @field:JsonProperty("day") val day: Int,
    @field:JsonProperty("challName") val challName: String,
    @field:JsonProperty("challType") val challType: ChallengeTypeDto,
    @field:JsonProperty("chall") val chall: String,
    @field:JsonProperty("description") val description: String?,
)
