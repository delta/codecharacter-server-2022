package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Update password request
 * @param oldPassword 
 * @param password 
 * @param passwordConfirmation 
 */
data class UpdatePasswordRequestDto(

    @get:Pattern(regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$")
    @get:Size(min=8,max=32)
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("oldPassword", required = true) val oldPassword: kotlin.String,

    @get:Pattern(regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$")
    @get:Size(min=8,max=32)
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("password", required = true) val password: kotlin.String,

    @get:Pattern(regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$")
    @get:Size(min=8,max=32)
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("passwordConfirmation", required = true) val passwordConfirmation: kotlin.String
) {

}

