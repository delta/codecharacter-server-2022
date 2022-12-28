package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Response model for daily challenge leaderboard
 * @param userName
 * @param score
 */
data class DailyChallengeLeaderBoardResponseDto(

    @Schema(example = "TestUser", description = "")
    @field:JsonProperty("userName") val userName: kotlin.String? = null,

    @Schema(example = "1500.0", description = "")
    @field:JsonProperty("score") val score: kotlin.String? = null
) {

}

