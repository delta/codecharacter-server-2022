package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.dtos.ChallengeTypeDto
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

