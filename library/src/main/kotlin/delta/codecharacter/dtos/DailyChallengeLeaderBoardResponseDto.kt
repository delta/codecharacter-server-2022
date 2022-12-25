package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Email
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import javax.validation.Valid
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

