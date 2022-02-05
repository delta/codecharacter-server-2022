package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty

/**
 * GameMap model
 * @param map
 * @param lastSavedAt
 */
data class GameMapDto(

    @ApiModelProperty(
        example = "0000\n0010\n0100\n1000\n",
        required = true,
        value = ""
    )
    @field:JsonProperty("map", required = true) val map: String,

    @ApiModelProperty(
        required = true,
        value = ""
    )
    @field:JsonProperty(
        "lastSavedAt",
        required = true
    ) val lastSavedAt: java.time.Instant
)
