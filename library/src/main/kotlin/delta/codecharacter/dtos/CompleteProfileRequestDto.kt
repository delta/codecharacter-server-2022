package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty

/**
 * Model for complete profile request
 * @param name
 * @param country
 * @param college
 * @param avatarId
 */
data class CompleteProfileRequestDto(

    @ApiModelProperty(example = "TestUser", required = true, value = "")
    @field:JsonProperty("username", required = true) val username: String,

    @ApiModelProperty(example = "Test User", required = true, value = "")
    @field:JsonProperty("name", required = true) val name: String,

    @ApiModelProperty(example = "IN", required = true, value = "")
    @field:JsonProperty("country", required = true) val country: String,

    @ApiModelProperty(example = "Test", required = true, value = "")
    @field:JsonProperty("college", required = true) val college: String,

    @ApiModelProperty(example = "1", required = true, value = "")
    @field:JsonProperty("avatarId", required = true) val avatarId: Int
)
