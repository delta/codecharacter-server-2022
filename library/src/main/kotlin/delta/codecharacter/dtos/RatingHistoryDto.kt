package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Email
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import javax.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Rating history model
 * @param rating 
 * @param ratingDeviation 
 * @param validFrom 
 */
data class RatingHistoryDto(

    @Schema(example = "1000", required = true, description = "")
    @field:JsonProperty("rating", required = true) val rating: java.math.BigDecimal,

    @Schema(example = "5", required = true, description = "")
    @field:JsonProperty("ratingDeviation", required = true) val ratingDeviation: java.math.BigDecimal,

    @Schema(example = "2021-01-01T00:00Z", required = true, description = "")
    @field:JsonProperty("validFrom", required = true) val validFrom: java.time.Instant
) {

}

