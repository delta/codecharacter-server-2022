package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import delta.codecharacter.dtos.LanguageDto
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Code model
 * @param code 
 * @param lastSavedAt 
 * @param language 
 */
data class CodeDto(

    @Schema(example = "#include <iostream>", required = true, description = "")
    @get:JsonProperty("code", required = true) val code: kotlin.String,

    @Schema(example = "2021-01-01T00:00Z", required = true, description = "")
    @get:JsonProperty("lastSavedAt", required = true) val lastSavedAt: java.time.Instant,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("language", required = true) val language: LanguageDto
) {

}

