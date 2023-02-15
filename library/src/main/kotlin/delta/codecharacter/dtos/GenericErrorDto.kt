package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Model for Generic Error
 * @param message 
 */
data class GenericErrorDto(

    @Schema(example = "null", description = "")
    @get:JsonProperty("message") val message: kotlin.String? = null
) {

}

