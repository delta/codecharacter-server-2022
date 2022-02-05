package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty

/**
 * Update latest map request
 * @param map
 * @param lock
 */
data class UpdateLatestMapRequestDto(

    @ApiModelProperty(
        example = "0000\n0010\n0100\n1000\n",
        required = true,
        value = ""
    )
    @field:JsonProperty("map", required = true) val map: String,

    @ApiModelProperty(example = "null", value = "")
    @field:JsonProperty("lock") val lock: Boolean? = false
)
