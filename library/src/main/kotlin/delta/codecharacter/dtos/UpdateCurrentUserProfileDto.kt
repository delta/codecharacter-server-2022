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
 * Update current user profile request
 * @param name 
 * @param country 
 * @param college 
 * @param avatarId 
 */
data class UpdateCurrentUserProfileDto(

    @Schema(example = "Test", description = "")
    @field:JsonProperty("name") val name: kotlin.String? = null,

    @Schema(example = "IN", description = "")
    @field:JsonProperty("country") val country: kotlin.String? = null,

    @Schema(example = "Test", description = "")
    @field:JsonProperty("college") val college: kotlin.String? = null,

    @Schema(example = "1", description = "")
    @field:JsonProperty("avatarId") val avatarId: kotlin.Int? = null
) {

}

