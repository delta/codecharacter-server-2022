package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty

/**
 * GameMap revision model
 * @param id
 * @param map
 * @param createdAt
 * @param message
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

    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty(
        "createdAt",
        required = true
    ) val createdAt: java.time.Instant,

    @ApiModelProperty(example = "message", required = true, value = "")
    @field:JsonProperty("message", required = true) val message: String,

    @ApiModelProperty(
        example = "123e4567-e89b-12d3-a456-426614174111",
        value = ""
    )
    @field:JsonProperty("parentRevision") val parentRevision: java.util.UUID? = null
)
