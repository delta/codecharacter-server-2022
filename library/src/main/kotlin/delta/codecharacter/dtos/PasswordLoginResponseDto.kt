package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty

/**
 * Login response with user token
 * @param token Bearer token
 */
data class PasswordLoginResponseDto(

    @ApiModelProperty(
        example = "test-token",
        required = true,
        value = "Bearer token"
    )
    @field:JsonProperty("token", required = true) val token: String
)
