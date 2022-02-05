package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty

/**
 * Activate user request
 * @param token
 */
data class ActivateUserRequestDto(

    @ApiModelProperty(example = "example-token", required = true, value = "")
    @field:JsonProperty("token", required = true) val token: String
)
