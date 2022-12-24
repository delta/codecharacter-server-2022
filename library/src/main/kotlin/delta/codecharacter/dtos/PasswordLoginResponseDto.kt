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
 * Login response with user token
 * @param token Bearer token
 */
data class PasswordLoginResponseDto(

    @Schema(example = "test-token", required = true, description = "Bearer token")
    @field:JsonProperty("token", required = true) val token: kotlin.String
) {

}

