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
 * User stats model
 * @param rating
 * @param wins
 * @param losses
 * @param ties
 */
data class UserStatsDto(

    @Schema(example = "1000", required = true, description = "")
    @field:JsonProperty("rating", required = true) val rating: java.math.BigDecimal,

    @Schema(example = "1", required = true, description = "")
    @field:JsonProperty("tier", required = true) val tier: Int,

    @Schema(example = "1", required = true, description = "")
    @field:JsonProperty("wins", required = true) val wins: kotlin.Int = 0,

    @Schema(example = "1", required = true, description = "")
    @field:JsonProperty("losses", required = true) val losses: kotlin.Int,

    @Schema(example = "1", required = true, description = "")
    @field:JsonProperty("ties", required = true) val ties: kotlin.Int
) {

}

