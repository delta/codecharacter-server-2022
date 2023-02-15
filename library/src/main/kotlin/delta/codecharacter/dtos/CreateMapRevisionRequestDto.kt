package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import delta.codecharacter.dtos.GameMapTypeDto
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Create map revision request
 * @param map 
 * @param mapImage 
 * @param message 
 * @param mapType 
 */
data class CreateMapRevisionRequestDto(

    @Schema(example = "0000\n0010\n0100\n1000\n", required = true, description = "")
    @get:JsonProperty("map", required = true) val map: kotlin.String,

    @Schema(example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAIAQMAAAD+wSzIAAAABlBMVEX///+/v7+jQ3Y5AAAADklEQVQI12P4AIX8EAgALgAD/aNpbtEAAAAASUVORK5CYII", required = true, description = "")
    @get:JsonProperty("mapImage", required = true) val mapImage: kotlin.String,

    @Schema(example = "message", required = true, description = "")
    @get:JsonProperty("message", required = true) val message: kotlin.String,

    @field:Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("mapType") val mapType: GameMapTypeDto? = GameMapTypeDto.NORMAL
) {

}

