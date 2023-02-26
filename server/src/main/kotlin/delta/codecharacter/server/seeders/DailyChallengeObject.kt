package delta.codecharacter.server.seeders

import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.dtos.ChallengeTypeDto
import delta.codecharacter.dtos.DailyChallengeObjectDto

data class DailyChallengeObject(
    @field:JsonProperty("day") val day: Int,
    @field:JsonProperty("challName") val challName: String,
    @field:JsonProperty("challType") val challType: ChallengeTypeDto,
    @field:JsonProperty("chall") val chall: DailyChallengeObjectDto,
    @field:JsonProperty("description") val description: String?,
    @field:JsonProperty("perfectScore") val perfectScore: Int,
    @field:JsonProperty("numberOfCompletions") val numberOfCompletions: Int,
    @field:JsonProperty("toleratedDestruction") val toleratedDestruction: Int,
    @field:JsonProperty("map") val map: String
)
