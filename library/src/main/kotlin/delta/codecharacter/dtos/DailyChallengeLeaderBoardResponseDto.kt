package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Response model for daily challenge leaderboard
 * @param userName 
 * @param score 
 * @param avatarId 
 */
data class DailyChallengeLeaderBoardResponseDto(

    @Schema(example = "TestUser", required = true, description = "")
    @get:JsonProperty("userName", required = true) val userName: kotlin.String,

    @Schema(example = "1500.0", required = true, description = "")
    @get:JsonProperty("score", required = true) val score: java.math.BigDecimal,

    @Schema(example = "1", required = true, description = "")
    @get:JsonProperty("avatarId", required = true) val avatarId: kotlin.Int
) {

}

