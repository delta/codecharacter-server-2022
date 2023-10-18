package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import delta.codecharacter.dtos.TierTypeDto
import jakarta.validation.constraints.*
import jakarta.validation.Valid
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
 * @param tutorialLevel 
 * @param isProfileComplete 
 * @param isTutorialComplete 
 * @param tier 
 */
data class CurrentUserProfileDto(

    @Schema(example = "123e4567-e89b-12d3-a456-426614174003", required = true, description = "")
    @get:JsonProperty("id", required = true) val id: java.util.UUID,

    @Schema(example = "test", required = true, description = "")
    @get:JsonProperty("username", required = true) val username: kotlin.String,

    @Schema(example = "Test", required = true, description = "")
    @get:JsonProperty("name", required = true) val name: kotlin.String,

    @get:Email
    @get:Pattern(regexp="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")
    @Schema(example = "test@test.com", required = true, description = "")
    @get:JsonProperty("email", required = true) val email: kotlin.String,

    @Schema(example = "IN", required = true, description = "")
    @get:JsonProperty("country", required = true) val country: kotlin.String,

    @Schema(example = "Test", required = true, description = "")
    @get:JsonProperty("college", required = true) val college: kotlin.String,

    @Schema(example = "1", required = true, description = "")
    @get:JsonProperty("avatarId", required = true) val avatarId: kotlin.Int,

    @Schema(example = "1", required = true, description = "")
    @get:JsonProperty("tutorialLevel", required = true) val tutorialLevel: kotlin.Int,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("isProfileComplete", required = true) val isProfileComplete: kotlin.Boolean = false,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("isTutorialComplete", required = true) val isTutorialComplete: kotlin.Boolean = false,

    @field:Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("tier") val tier: TierTypeDto? = null
) {

}

