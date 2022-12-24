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
 * Code revision model
 * @param id 
 * @param code 
 * @param message 
 * @param language 
 * @param createdAt 
 * @param parentRevision 
 */
data class CodeRevisionDto(

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000", required = true, description = "")
    @field:JsonProperty("id", required = true) val id: java.util.UUID,

    @Schema(example = "#include <iostream>", required = true, description = "")
    @field:JsonProperty("code", required = true) val code: kotlin.String,

    @Schema(example = "message", required = true, description = "")
    @field:JsonProperty("message", required = true) val message: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("language", required = true) val language: LanguageDto,

    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("createdAt", required = true) val createdAt: java.time.Instant,

    @Schema(example = "123e4567-e89b-12d3-a456-426614174111", description = "")
    @field:JsonProperty("parentRevision") val parentRevision: java.util.UUID? = null
) {

}

