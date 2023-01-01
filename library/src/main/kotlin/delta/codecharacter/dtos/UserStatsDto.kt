package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

/**
 * User stats model
 * @param rating
 * @param wins
 * @param losses
 * @param ties
 */
data class UserStatsDto(

        @Schema(example = "1000", required = true, description = "")
        @field:JsonProperty("rating", required = true) val rating: BigDecimal,

        @Schema(example = "1", required = true, description = "")
        @field:JsonProperty("wins", required = true) val wins: Int = 0,

        @Schema(example = "1", required = true, description = "")
        @field:JsonProperty("losses", required = true) val losses: Int,

        @Schema(example = "1", required = true, description = "")
        @field:JsonProperty("ties", required = true) val ties: Int
) {

}

