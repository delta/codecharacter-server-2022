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
 * Update latest map request
 * @param map 
 * @param lock 
 */
data class UpdateLatestMapRequestDto(

    @Schema(example = "0000\n0010\n0100\n1000\n", required = true, description = "")
    @field:JsonProperty("map", required = true) val map: kotlin.String,

    @Schema(example = "null", description = "")
    @field:JsonProperty("lock") val lock: kotlin.Boolean? = false
) {

}

