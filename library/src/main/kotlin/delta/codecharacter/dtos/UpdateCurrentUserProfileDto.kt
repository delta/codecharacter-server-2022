package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.dtos.TutorialUpdateTypeDto
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
 * Update current user profile request
 * @param name
 * @param country
 * @param college
 * @param avatarId
 * @param updateTutorialLevel
 */
data class UpdateCurrentUserProfileDto(

    @Schema(example = "Test", description = "")
    @field:JsonProperty("name") val name: kotlin.String? = null,

    @Schema(example = "IN", description = "")
    @field:JsonProperty("country") val country: kotlin.String? = null,

    @Schema(example = "Test", description = "")
    @field:JsonProperty("college") val college: kotlin.String? = null,

    @Schema(example = "1", description = "")
    @field:JsonProperty("avatarId") val avatarId: kotlin.Int? = null,

    @field:Valid
    @Schema(example = "null", description = "")
    @field:JsonProperty("updateTutorialLevel") val updateTutorialLevel: TutorialUpdateTypeDto? = null
) {

}

