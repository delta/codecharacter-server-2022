package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.dtos.GameStatusDto
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
 * Game model
 * @param id
 * @param destruction
 * @param coinsUsed
 * @param status
 */
data class GameDto(

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000", required = true, description = "")
    @field:JsonProperty("id", required = true) val id: java.util.UUID,

    @Schema(example = "100", required = true, description = "")
    @field:JsonProperty("destruction", required = true) val destruction: java.math.BigDecimal,

    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("coinsUsed", required = true) val coinsUsed: kotlin.Int,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("status", required = true) val status: GameStatusDto
) {

}

