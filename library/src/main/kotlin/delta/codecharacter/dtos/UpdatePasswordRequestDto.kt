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
 * Update password request
 * @param oldPassword 
 * @param password 
 * @param passwordConfirmation 
 */
data class UpdatePasswordRequestDto(

    @get:Pattern(regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$")
    @get:Size(min=8,max=32)
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("oldPassword", required = true) val oldPassword: kotlin.String,

    @get:Pattern(regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$")
    @get:Size(min=8,max=32)
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("password", required = true) val password: kotlin.String,

    @get:Pattern(regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$")
    @get:Size(min=8,max=32)
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("passwordConfirmation", required = true) val passwordConfirmation: kotlin.String
) {

}

