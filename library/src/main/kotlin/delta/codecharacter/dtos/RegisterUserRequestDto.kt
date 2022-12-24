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
 * Register user request
 * @param username 
 * @param name 
 * @param email 
 * @param password 
 * @param passwordConfirmation 
 * @param country 
 * @param college 
 * @param avatarId 
 */
data class RegisterUserRequestDto(

    @Schema(example = "test", required = true, description = "")
    @field:JsonProperty("username", required = true) val username: kotlin.String,

    @Schema(example = "Test", required = true, description = "")
    @field:JsonProperty("name", required = true) val name: kotlin.String,

    @get:Email
    @get:Pattern(regexp="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")
    @Schema(example = "test@test.com", required = true, description = "")
    @field:JsonProperty("email", required = true) val email: kotlin.String,

    @get:Pattern(regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$")
    @get:Size(min=8,max=32)
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("password", required = true) val password: kotlin.String,

    @get:Pattern(regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$")
    @get:Size(min=8,max=32)
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("passwordConfirmation", required = true) val passwordConfirmation: kotlin.String,

    @Schema(example = "IN", required = true, description = "")
    @field:JsonProperty("country", required = true) val country: kotlin.String,

    @Schema(example = "Test", required = true, description = "")
    @field:JsonProperty("college", required = true) val college: kotlin.String,

    @Schema(example = "1", required = true, description = "")
    @field:JsonProperty("avatarId", required = true) val avatarId: kotlin.Int
) {

}

