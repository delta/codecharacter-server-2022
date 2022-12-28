package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
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
 * Register user request
 * @param username
 * @param name
 * @param email
 * @param password
 * @param passwordConfirmation
 * @param country
 * @param college
 * @param avatarId
 * @param recaptchaCode
 */
data class RegisterUserRequestDto(

    @Schema(example = "test", required = true, description = "")
    @field:JsonProperty("username", required = true) val username: kotlin.String,

    @Schema(example = "Test", required = true, description = "")
    @field:JsonProperty("name", required = true) val name: kotlin.String,

    @get:Email
    @get:Pattern(regexp="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")
    @Schema(example = "test@test.com", required = true, description = "")
    @field:JsonProperty("email", required = true) val email: kotlin.String,

    @get:Pattern(regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$")
    @get:Size(min=8,max=32)
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("password", required = true) val password: kotlin.String,

    @get:Pattern(regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,32}$")
    @get:Size(min=8,max=32)
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("passwordConfirmation", required = true) val passwordConfirmation: kotlin.String,

    @Schema(example = "IN", required = true, description = "")
    @field:JsonProperty("country", required = true) val country: kotlin.String,

    @Schema(example = "Test", required = true, description = "")
    @field:JsonProperty("college", required = true) val college: kotlin.String,

    @Schema(example = "1", required = true, description = "")
    @field:JsonProperty("avatarId", required = true) val avatarId: kotlin.Int,

    @Schema(example = "03AD1IbLAGl_UdwYP3-AeibnfJgXy_g3cNr_rhkBBh4zalD9GEXAR2xCcUGi7WlxFgOjYlpbRpZFTJJDVugJF-H4pBl32DU619cYHplp_ReGiOokgvz8DwiRLIZBvg1eu2e77jihWQPndoWU_WOTKrYVq1mzBcdPUfJ3PEMCo-eGvoyRaNvRWE0JYBSBgDfwFBaw8RmxaqiS84or-_G7-TDiifFYpcNFiIolIjGi9DkbMXivkjiIoEomAz6NUHg0alrk0C5_p1maoErBmpwLGwlAgKL_sa-ZAzHb89OprdVI8BXtN0CATBgwYO6u_zqrK5N9wDQyh-OmtFh5RXkEzmkASls33UYcJrtMfeFU-b9N-u-Je6NXVfkX49gAGan3k-GqkgcFKHowc5Cwym9tlGLrfiBtqKLIADw1UX4BCbIx9BbHlesoKEubr7MoVZCDv3VfctSTMXG-oH5IbDRQhez4E6JHR4Uv0lWyHKROv7wdxqXauz5PBlUlE11BdffXU5NEssJkM4Tk3zg5k6ddkju8DU2keqXodnzXVTBIXC6zxriA8IHaS_KFBtazAYZ6oac3-5Y2VMwli3XaADBCCVJzXC0GTa1jeuZQ", required = true, description = "")
    @field:JsonProperty("recaptchaCode", required = true) val recaptchaCode: kotlin.String
) {

}

