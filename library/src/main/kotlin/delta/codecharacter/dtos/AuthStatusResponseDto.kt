package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 * @param status
 */
data class AuthStatusResponseDto(

    @Schema(example = "null", description = "")
    @field:JsonProperty("status") val status: AuthStatusResponseDto.Status? = null
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

