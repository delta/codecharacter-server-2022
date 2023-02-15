package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Activate user request
 * @param token 
 */
data class ActivateUserRequestDto(

    @Schema(example = "example-token", required = true, description = "")
    @get:JsonProperty("token", required = true) val token: kotlin.String
) {

}

