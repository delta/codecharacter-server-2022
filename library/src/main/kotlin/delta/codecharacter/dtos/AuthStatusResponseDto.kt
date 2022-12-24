package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
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

