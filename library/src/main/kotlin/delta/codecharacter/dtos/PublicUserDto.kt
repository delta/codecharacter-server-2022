package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
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
 * Public user model
 * @param username
 * @param name
 * @param country
 * @param college
 * @param avatarId
 */
data class PublicUserDto(

    @Schema(example = "test", required = true, description = "")
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

