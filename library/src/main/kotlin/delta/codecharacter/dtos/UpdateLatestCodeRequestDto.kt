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
 * Update latest code request
 * @param code 
 * @param language 
 * @param lock 
 */
data class UpdateLatestCodeRequestDto(

    @Schema(example = "#include <iostream>", required = true, description = "")
    @field:JsonProperty("code", required = true) val code: kotlin.String,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("language", required = true) val language: LanguageDto,

    @Schema(example = "null", description = "")
    @field:JsonProperty("lock") val lock: kotlin.Boolean? = false
) {

}

