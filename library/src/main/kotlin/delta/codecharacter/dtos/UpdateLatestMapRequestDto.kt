package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import delta.codecharacter.dtos.GameMapTypeDto
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Update latest map request
 * @param map 
 * @param mapImage 
 * @param mapType 
 * @param lock 
 */
data class UpdateLatestMapRequestDto(

    @Schema(example = "0000\n0010\n0100\n1000\n", required = true, description = "")
    @get:JsonProperty("map", required = true) val map: kotlin.String,

    @Schema(example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAIAQMAAAD+wSzIAAAABlBMVEX///+/v7+jQ3Y5AAAADklEQVQI12P4AIX8EAgALgAD/aNpbtEAAAAASUVORK5CYII", required = true, description = "")
    @get:JsonProperty("mapImage", required = true) val mapImage: kotlin.String,

    @field:Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("mapType") val mapType: GameMapTypeDto? = GameMapTypeDto.NORMAL,

    @Schema(example = "null", description = "")
    @get:JsonProperty("lock") val lock: kotlin.Boolean? = false
) {

}

