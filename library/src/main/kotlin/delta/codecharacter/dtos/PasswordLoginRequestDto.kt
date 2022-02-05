package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

/**
 * Password Login request
 * @param email
 * @param password
 */
data class PasswordLoginRequestDto(
    @get:Pattern(regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")
    @ApiModelProperty(example = "test@test.com", required = true, value = "")
    @field:JsonProperty("email", required = true) val email: String,
    @get:Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$")
    @get:Size(min = 8, max = 32)
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty("password", required = true) val password: String
)
