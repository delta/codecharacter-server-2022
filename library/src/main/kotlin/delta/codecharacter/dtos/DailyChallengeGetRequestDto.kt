package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import delta.codecharacter.dtos.ChallengeTypeDto
import delta.codecharacter.dtos.DailyChallengeObjectDto
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Get current-user daily challenge
 * @param challName 
 * @param chall 
 * @param challType 
 * @param description 
 * @param completionStatus 
 */
data class DailyChallengeGetRequestDto(

    @Schema(example = "Daily Challenge 1", required = true, description = "")
    @get:JsonProperty("challName", required = true) val challName: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("chall", required = true) val chall: DailyChallengeObjectDto,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("challType", required = true) val challType: ChallengeTypeDto,

    @Schema(example = "Daily Challenge description", description = "")
    @get:JsonProperty("description") val description: kotlin.String? = null,

    @Schema(example = "true", description = "")
    @get:JsonProperty("completionStatus") val completionStatus: kotlin.Boolean? = null
) {

}

