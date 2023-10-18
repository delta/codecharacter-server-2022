package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Get map image and map by commitId
 * @param mapImage 
 * @param map 
 */
data class MapCommitByCommitIdResponseDto(

    @Schema(example = "base-64-string", required = true, description = "")
    @get:JsonProperty("mapImage", required = true) val mapImage: kotlin.String,

    @Schema(example = "0000\n0000\n0001\n0000", required = true, description = "")
    @get:JsonProperty("map", required = true) val map: kotlin.String
) {

}

