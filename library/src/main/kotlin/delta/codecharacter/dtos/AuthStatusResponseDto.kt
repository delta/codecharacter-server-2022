package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty

/**
 *
 * @param status
 */
data class AuthStatusResponseDto(

    @ApiModelProperty(example = "null", value = "")
    @field:JsonProperty("status") val status: Status? = null
) {

    /**
     *
     * Values: AUTHENTICATED,PROFILE_INCOMPLETE,ACTIVATION_PENDING
     */
    enum class Status(val value: String) {

        @JsonProperty("AUTHENTICATED")
        AUTHENTICATED("AUTHENTICATED"),

        @JsonProperty("PROFILE_INCOMPLETE")
        PROFILE_INCOMPLETE("PROFILE_INCOMPLETE"),

        @JsonProperty("ACTIVATION_PENDING")
        ACTIVATION_PENDING("ACTIVATION_PENDING");
    }
}
