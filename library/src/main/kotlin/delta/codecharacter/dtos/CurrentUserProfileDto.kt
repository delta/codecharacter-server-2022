package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.constraints.Pattern

/**
 * Current user profile model
 * @param id
 * @param username
 * @param name
 * @param email
 * @param country
 * @param college
 * @param avatarId
 * @param isProfileComplete
 */
data class CurrentUserProfileDto(

    @ApiModelProperty(
        example = "123e4567-e89b-12d3-a456-426614174003",
        required = true,
        value = ""
    )
    @field:JsonProperty("id", required = true) val id: java.util.UUID,

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

    @ApiModelProperty(example = "IN", required = true, value = "")
    @field:JsonProperty("country", required = true) val country: String,

    @ApiModelProperty(example = "Test", required = true, value = "")
    @field:JsonProperty("college", required = true) val college: String,

    @ApiModelProperty(example = "1", required = true, value = "")
    @field:JsonProperty("avatarId", required = true) val avatarId: Int,

    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty(
        "isProfileComplete",
        required = true
    ) val isProfileComplete: Boolean = false
)
