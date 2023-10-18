package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Login response with user token
 * @param token Bearer token
 */
data class PasswordLoginResponseDto(

    @Schema(example = "test-token", required = true, description = "Bearer token")
    @get:JsonProperty("token", required = true) val token: kotlin.String
) {

}

