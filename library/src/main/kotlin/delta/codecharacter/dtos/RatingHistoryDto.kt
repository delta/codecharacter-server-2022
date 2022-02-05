package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty

/**
 * Rating history model
 * @param rating
 * @param ratingDeviation
 * @param validFrom
 */
data class RatingHistoryDto(

    @ApiModelProperty(example = "1000", required = true, value = "")
    @field:JsonProperty(
        "rating",
        required = true
    ) val rating: java.math.BigDecimal,

    @ApiModelProperty(example = "5", required = true, value = "")
    @field:JsonProperty(
        "ratingDeviation",
        required = true
    ) val ratingDeviation: java.math.BigDecimal,

    @ApiModelProperty(
        required = true,
        value = ""
    )
    @field:JsonProperty(
        "validFrom",
        required = true
    ) val validFrom: java.time.Instant
)
