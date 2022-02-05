package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

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

    @ApiModelProperty(example = "test", required = true, value = "")
    @field:JsonProperty(
        "username",
        required = true
    ) val username: String,

    @ApiModelProperty(example = "Test", required = true, value = "")
    @field:JsonProperty("name", required = true) val name: String,
    @get:Pattern(regexp = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")
    @ApiModelProperty(example = "test@test.com", required = true, value = "")
    @field:JsonProperty("email", required = true) val email: String,
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
    ) val passwordConfirmation: String,

    @ApiModelProperty(example = "IN", required = true, value = "")
    @field:JsonProperty("country", required = true) val country: String,

    @ApiModelProperty(example = "Test", required = true, value = "")
    @field:JsonProperty("college", required = true) val college: String,

    @ApiModelProperty(example = "1", required = true, value = "")
    @field:JsonProperty("avatarId", required = true) val avatarId: Int
)
