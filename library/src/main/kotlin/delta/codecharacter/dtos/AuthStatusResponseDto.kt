package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * 
 * @param status 
 */
data class AuthStatusResponseDto(

    @Schema(example = "null", description = "")
    @get:JsonProperty("status") val status: AuthStatusResponseDto.Status? = null
) {

    /**
    * 
    * Values: AUTHENTICATED,PROFILE_INCOMPLETE,ACTIVATION_PENDING
    */
    enum class Status(val value: kotlin.String) {

        @JsonProperty("AUTHENTICATED") AUTHENTICATED("AUTHENTICATED"),
        @JsonProperty("PROFILE_INCOMPLETE") PROFILE_INCOMPLETE("PROFILE_INCOMPLETE"),
        @JsonProperty("ACTIVATION_PENDING") ACTIVATION_PENDING("ACTIVATION_PENDING")
    }

}

