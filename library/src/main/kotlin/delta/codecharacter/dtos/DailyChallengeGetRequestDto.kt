package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.dtos.ChallengeTypeDto
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
 * Get current-user daily challenge
 * @param challName
 * @param chall
 * @param challType
 */
data class DailyChallengeGetRequestDto(

    @Schema(example = "Daily Challenge 1", required = true, description = "")
    @field:JsonProperty("challName", required = true) val challName: kotlin.String,

    @Schema(example = "print(\"hello world\");", required = true, description = "")
    @field:JsonProperty("chall", required = true) val chall: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("challType", required = true) val challType: ChallengeTypeDto
) {

}

