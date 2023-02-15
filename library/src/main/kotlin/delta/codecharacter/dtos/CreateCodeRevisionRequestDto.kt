package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import delta.codecharacter.dtos.CodeTypeDto
import delta.codecharacter.dtos.LanguageDto
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Create code revision request
 * @param code 
 * @param message 
 * @param language 
 * @param codeType 
 */
data class CreateCodeRevisionRequestDto(

    @Schema(example = "#include <iostream>", required = true, description = "")
    @get:JsonProperty("code", required = true) val code: kotlin.String,

    @Schema(example = "message", required = true, description = "")
    @get:JsonProperty("message", required = true) val message: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("language", required = true) val language: LanguageDto,

    @field:Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("codeType") val codeType: CodeTypeDto? = CodeTypeDto.NORMAL
) {

}

