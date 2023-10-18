package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * GameMap model
 * @param map 
 * @param mapImage 
 * @param lastSavedAt 
 */
data class GameMapDto(

    @Schema(example = "0000\n0010\n0100\n1000\n", required = true, description = "")
    @get:JsonProperty("map", required = true) val map: kotlin.String,

    @Schema(example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAIAQMAAAD+wSzIAAAABlBMVEX///+/v7+jQ3Y5AAAADklEQVQI12P4AIX8EAgALgAD/aNpbtEAAAAASUVORK5CYII", required = true, description = "")
    @get:JsonProperty("mapImage", required = true) val mapImage: kotlin.String,

    @Schema(example = "2021-01-01T00:00Z", required = true, description = "")
    @get:JsonProperty("lastSavedAt", required = true) val lastSavedAt: java.time.Instant
) {

}

