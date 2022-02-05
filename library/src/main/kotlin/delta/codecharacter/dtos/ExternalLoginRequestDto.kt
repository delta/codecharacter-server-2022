package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty

/**
 * External Login request
 * @param provider
 */
data class ExternalLoginRequestDto(

    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty(
        "provider",
        required = true
    ) val provider: Provider
) {

    /**
     *
     * Values: GOOGLE,GITHUB
     */
    enum class Provider(val value: String) {

        @JsonProperty("GOOGLE")
        GOOGLE("GOOGLE"),

        @JsonProperty("GITHUB")
        GITHUB("GITHUB");
    }
}
