package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty

/**
 * User stats model
 * @param rating
 * @param wins
 * @param losses
 * @param ties
 */
data class UserStatsDto(

    @ApiModelProperty(example = "1000", required = true, value = "")
    @field:JsonProperty(
        "rating",
        required = true
    ) val rating: java.math.BigDecimal,

    @ApiModelProperty(example = "1", required = true, value = "")
    @field:JsonProperty("wins", required = true) val wins: Int = 0,

    @ApiModelProperty(example = "1", required = true, value = "")
    @field:JsonProperty("losses", required = true) val losses: Int,

    @ApiModelProperty(example = "1", required = true, value = "")
    @field:JsonProperty("ties", required = true) val ties: Int
)
