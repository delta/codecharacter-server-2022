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
 * Get map image and map by commitId
 * @param mapImage
 * @param map
 */
data class MapCommitByCommitIdResponseDto(

    @Schema(example = "base-64-string", required = true, description = "")
    @field:JsonProperty("mapImage", required = true) val mapImage: kotlin.String,

    @Schema(example = "0000\n0000\n0001\n0000", required = true, description = "")
    @field:JsonProperty("map", required = true) val map: kotlin.String
) {

}

