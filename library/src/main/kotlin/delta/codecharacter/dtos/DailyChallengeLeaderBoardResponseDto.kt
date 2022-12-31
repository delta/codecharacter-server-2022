package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Response model for daily challenge leaderboard
 * @param userName
 * @param score
 */
data class DailyChallengeLeaderBoardResponseDto(

    @Schema(example = "TestUser", description = "")
    @field:JsonProperty("userName") val userName: String? = null,

    @Schema(example = "1500.0", description = "")
    @field:JsonProperty("score") val score: Int? = null
) {
}

