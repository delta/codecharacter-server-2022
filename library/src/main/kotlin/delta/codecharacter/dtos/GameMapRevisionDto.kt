package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * GameMap revision model
 * @param id 
 * @param map 
 * @param createdAt 
 * @param message 
 * @param parentRevision 
 */
data class GameMapRevisionDto(

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000", required = true, description = "")
    @get:JsonProperty("id", required = true) val id: java.util.UUID,

    @Schema(example = "0000\n0010\n0100\n1000\n", required = true, description = "")
    @get:JsonProperty("map", required = true) val map: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("createdAt", required = true) val createdAt: java.time.Instant,

    @Schema(example = "message", required = true, description = "")
    @get:JsonProperty("message", required = true) val message: kotlin.String,

    @Schema(example = "123e4567-e89b-12d3-a456-426614174111", description = "")
    @get:JsonProperty("parentRevision") val parentRevision: java.util.UUID? = null
) {

}

