package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.Valid
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

