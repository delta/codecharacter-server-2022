package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import delta.codecharacter.dtos.TutorialUpdateTypeDto
import jakarta.validation.constraints.*
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
    @get:JsonProperty("name") val name: kotlin.String? = null,

    @Schema(example = "IN", description = "")
    @get:JsonProperty("country") val country: kotlin.String? = null,

    @Schema(example = "Test", description = "")
    @get:JsonProperty("college") val college: kotlin.String? = null,

    @Schema(example = "1", description = "")
    @get:JsonProperty("avatarId") val avatarId: kotlin.Int? = null,

    @field:Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("updateTutorialLevel") val updateTutorialLevel: TutorialUpdateTypeDto? = null
) {

}

