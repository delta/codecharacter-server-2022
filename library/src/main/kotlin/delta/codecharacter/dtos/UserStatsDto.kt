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
    @field:JsonProperty("wins", required = true) val wins: kotlin.Int = 0,

    @Schema(example = "1", required = true, description = "")
    @field:JsonProperty("losses", required = true) val losses: kotlin.Int,

    @Schema(example = "1", required = true, description = "")
    @field:JsonProperty("ties", required = true) val ties: kotlin.Int
) {

}

