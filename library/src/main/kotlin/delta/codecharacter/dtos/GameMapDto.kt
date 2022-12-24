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
 * GameMap model
 * @param map 
 * @param lastSavedAt 
 */
data class GameMapDto(

    @Schema(example = "0000\n0010\n0100\n1000\n", required = true, description = "")
    @field:JsonProperty("map", required = true) val map: kotlin.String,

    @Schema(example = "2021-01-01T00:00Z", required = true, description = "")
    @field:JsonProperty("lastSavedAt", required = true) val lastSavedAt: java.time.Instant
) {

}

