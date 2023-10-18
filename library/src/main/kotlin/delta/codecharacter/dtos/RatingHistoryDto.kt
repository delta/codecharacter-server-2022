package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
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
    @get:JsonProperty("rating", required = true) val rating: java.math.BigDecimal,

    @Schema(example = "5", required = true, description = "")
    @get:JsonProperty("ratingDeviation", required = true) val ratingDeviation: java.math.BigDecimal,

    @Schema(example = "2021-01-01T00:00Z", required = true, description = "")
    @get:JsonProperty("validFrom", required = true) val validFrom: java.time.Instant
) {

}

