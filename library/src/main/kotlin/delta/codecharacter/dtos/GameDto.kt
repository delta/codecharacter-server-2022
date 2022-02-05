package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.Valid

/**
 * Game model
 * @param id
 * @param points1
 * @param points2
 * @param status
 * @param gameVerdict
 * @param map
 */
data class GameDto(

    @ApiModelProperty(
        example = "123e4567-e89b-12d3-a456-426614174000",
        required = true,
        value = ""
    )
    @field:JsonProperty("id", required = true) val id: java.util.UUID,

    @ApiModelProperty(example = "100", required = true, value = "")
    @field:JsonProperty("points1", required = true) val points1: Int,

    @ApiModelProperty(example = "90", required = true, value = "")
    @field:JsonProperty("points2", required = true) val points2: Int,

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty("status", required = true) val status: GameStatusDto,

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty(
        "gameVerdict",
        required = true
    ) val gameVerdict: VerdictDto,

    @ApiModelProperty(example = "0000\n0010\n0100\n1000\n", value = "")
    @field:JsonProperty("map") val map: String? = null
)
