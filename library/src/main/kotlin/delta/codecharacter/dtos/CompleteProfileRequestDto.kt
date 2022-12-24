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
 * Model for complete profile request
 * @param username 
 * @param name 
 * @param country 
 * @param college 
 * @param avatarId 
 */
data class CompleteProfileRequestDto(

    @Schema(example = "TestUser", required = true, description = "")
    @field:JsonProperty("username", required = true) val username: kotlin.String,

    @Schema(example = "Test User", required = true, description = "")
    @field:JsonProperty("name", required = true) val name: kotlin.String,

    @Schema(example = "IN", required = true, description = "")
    @field:JsonProperty("country", required = true) val country: kotlin.String,

    @Schema(example = "Test", required = true, description = "")
    @field:JsonProperty("college", required = true) val college: kotlin.String,

    @Schema(example = "1", required = true, description = "")
    @field:JsonProperty("avatarId", required = true) val avatarId: kotlin.Int
) {

}

