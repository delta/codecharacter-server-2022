package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Model for complete profile request
 * @param username 
 * @param name 
 * @param country 
 * @param college 
 * @param avatarId 
 */
data class CompleteProfileRequestDto(

    @Schema(example = "TestUser", required = true, description = "")
    @get:JsonProperty("username", required = true) val username: kotlin.String,

    @Schema(example = "Test User", required = true, description = "")
    @get:JsonProperty("name", required = true) val name: kotlin.String,

    @Schema(example = "IN", required = true, description = "")
    @get:JsonProperty("country", required = true) val country: kotlin.String,

    @Schema(example = "Test", required = true, description = "")
    @get:JsonProperty("college", required = true) val college: kotlin.String,

    @Schema(example = "1", required = true, description = "")
    @get:JsonProperty("avatarId", required = true) val avatarId: kotlin.Int
) {

}

