package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

/**
 * Update password request
 * @param oldPassword
 * @param password
 * @param passwordConfirmation
 */
data class UpdatePasswordRequestDto(
    @get:Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$")
    @get:Size(min = 8, max = 32)
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty(
        "oldPassword",
        required = true
    ) val oldPassword: String,
    @get:Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$")
    @get:Size(min = 8, max = 32)
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty(
        "password",
        required = true
    ) val password: String,
    @get:Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$")
    @get:Size(min = 8, max = 32)
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty(
        "passwordConfirmation",
        required = true
    ) val passwordConfirmation: String
)
