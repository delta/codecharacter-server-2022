package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
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
    @get:JsonProperty("rating", required = true) val rating: java.math.BigDecimal,

    @Schema(example = "1", required = true, description = "")
    @get:JsonProperty("wins", required = true) val wins: kotlin.Int = 0,

    @Schema(example = "1", required = true, description = "")
    @get:JsonProperty("losses", required = true) val losses: kotlin.Int,

    @Schema(example = "1", required = true, description = "")
    @get:JsonProperty("ties", required = true) val ties: kotlin.Int
) {

}

