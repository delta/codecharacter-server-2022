package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty

/**
 * Create map revision request
 * @param map
 */
data class CreateMapRevisionRequestDto(

    @ApiModelProperty(
        example = "0000\n0010\n0100\n1000\n",
        required = true,
        value = ""
    )
    @field:JsonProperty("map", required = true) val map: String
)
