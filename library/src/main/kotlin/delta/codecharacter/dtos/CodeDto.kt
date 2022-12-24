package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.dtos.LanguageDto
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
 * Code model
 * @param code 
 * @param lastSavedAt 
 * @param language 
 */
data class CodeDto(

    @Schema(example = "#include <iostream>", required = true, description = "")
    @field:JsonProperty("code", required = true) val code: kotlin.String,

    @Schema(example = "2021-01-01T00:00Z", required = true, description = "")
    @field:JsonProperty("lastSavedAt", required = true) val lastSavedAt: java.time.Instant,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("language", required = true) val language: LanguageDto
) {

}

