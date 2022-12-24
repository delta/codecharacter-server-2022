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
 * GameMap revision model
 * @param id 
 * @param map 
 * @param createdAt 
 * @param message 
 * @param parentRevision 
 */
data class GameMapRevisionDto(

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000", required = true, description = "")
    @field:JsonProperty("id", required = true) val id: java.util.UUID,

    @Schema(example = "0000\n0010\n0100\n1000\n", required = true, description = "")
    @field:JsonProperty("map", required = true) val map: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("createdAt", required = true) val createdAt: java.time.Instant,

    @Schema(example = "message", required = true, description = "")
    @field:JsonProperty("message", required = true) val message: kotlin.String,

    @Schema(example = "123e4567-e89b-12d3-a456-426614174111", description = "")
    @field:JsonProperty("parentRevision") val parentRevision: java.util.UUID? = null
) {

}

