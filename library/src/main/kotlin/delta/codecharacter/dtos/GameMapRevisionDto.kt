package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty

/**
 * GameMap revision model
 * @param id
 * @param map
 * @param parentRevision
 */
data class GameMapRevisionDto(

    @ApiModelProperty(
        example = "123e4567-e89b-12d3-a456-426614174000",
        required = true,
        value = ""
    )
    @field:JsonProperty("id", required = true) val id: java.util.UUID,

    @ApiModelProperty(
        example = "0000\n0010\n0100\n1000\n",
        required = true,
        value = ""
    )
    @field:JsonProperty("map", required = true) val map: String,

    @ApiModelProperty(
        example = "123e4567-e89b-12d3-a456-426614174111",
        required = true,
        value = ""
    )
    @field:JsonProperty(
        "parentRevision",
        required = true
    ) val parentRevision: java.util.UUID
)
