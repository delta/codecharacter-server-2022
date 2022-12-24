package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.dtos.GameStatusDto
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

