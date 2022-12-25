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
 * Current user profile model
 * @param id 
 * @param username 
 * @param name 
 * @param email 
 * @param country 
 * @param college 
 * @param avatarId 
 * @param isProfileComplete 
 * @param isTutorialComplete 
 */
data class CurrentUserProfileDto(

    @Schema(example = "123e4567-e89b-12d3-a456-426614174003", required = true, description = "")
    @field:JsonProperty("id", required = true) val id: java.util.UUID,

    @Schema(example = "test", required = true, description = "")
    @field:JsonProperty("username", required = true) val username: kotlin.String,

    @Schema(example = "Test", required = true, description = "")
    @field:JsonProperty("name", required = true) val name: kotlin.String,

    @get:Email
    @get:Pattern(regexp="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")
    @Schema(example = "test@test.com", required = true, description = "")
    @field:JsonProperty("email", required = true) val email: kotlin.String,

    @Schema(example = "IN", required = true, description = "")
    @field:JsonProperty("country", required = true) val country: kotlin.String,

    @Schema(example = "Test", required = true, description = "")
    @field:JsonProperty("college", required = true) val college: kotlin.String,

    @Schema(example = "1", required = true, description = "")
    @field:JsonProperty("avatarId", required = true) val avatarId: kotlin.Int,

    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("isProfileComplete", required = true) val isProfileComplete: kotlin.Boolean = false,

    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("isTutorialComplete", required = true) val isTutorialComplete: kotlin.Boolean = false
) {

}

