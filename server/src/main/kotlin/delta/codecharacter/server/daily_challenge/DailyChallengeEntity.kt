package delta.codecharacter.server.daily_challenge

import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.dtos.ChallengeTypeDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document(collection = "daily_challenges")
data class DailyChallengeEntity(
        @Id val id: UUID,
        @field:JsonProperty("day") val day: Int,
        @field:JsonProperty("challName") val challName: String,
        @field:JsonProperty("challType") val challType: ChallengeTypeDto,
        @field:JsonProperty("chall") val chall: String,
        @field:JsonProperty("description") val description: String?,
)
