package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Forgot password request
 * @param email 
 * @param recaptchaCode 
 */
data class ForgotPasswordRequestDto(

    @get:Email
    @get:Pattern(regexp="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")
    @Schema(example = "test@test.com", required = true, description = "")
    @get:JsonProperty("email", required = true) val email: kotlin.String,

    @Schema(example = "example recaptcha-code", description = "")
    @get:JsonProperty("recaptchaCode") val recaptchaCode: kotlin.String? = null
) {

}

