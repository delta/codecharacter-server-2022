package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.Valid

/**
 * Game model
 * @param id
 * @param destruction
 * @param coinsUsed
 * @param status
 * @param gameVerdict
 */
data class GameDto(

    @ApiModelProperty(
        example = "123e4567-e89b-12d3-a456-426614174000",
        required = true,
        value = ""
    )
    @field:JsonProperty("id", required = true) val id: java.util.UUID,

    @ApiModelProperty(example = "100", required = true, value = "")
    @field:JsonProperty(
        "destruction",
        required = true
    ) val destruction: java.math.BigDecimal,

    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty("coinsUsed", required = true) val coinsUsed: Int,

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty("status", required = true) val status: GameStatusDto,

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty(
        "gameVerdict",
        required = true
    ) val gameVerdict: VerdictDto
)
