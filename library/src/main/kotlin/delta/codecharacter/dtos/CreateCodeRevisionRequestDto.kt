package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.Valid

/**
 * Create code revision request
 * @param code
 * @param message
 * @param language
 */
data class CreateCodeRevisionRequestDto(

    @ApiModelProperty(
        example = "#include <iostream>",
        required = true,
        value = ""
    )
    @field:JsonProperty("code", required = true) val code: String,

    @ApiModelProperty(example = "message", required = true, value = "")
    @field:JsonProperty("message", required = true) val message: String,

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty("language", required = true) val language: LanguageDto
)
