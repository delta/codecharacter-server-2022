package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.Valid

/**
 * Code model
 * @param code
 * @param lastSavedAt
 * @param language
 */
data class CodeDto(

    @ApiModelProperty(
        example = "#include <iostream>",
        required = true,
        value = ""
    )
    @field:JsonProperty("code", required = true) val code: String,

    @ApiModelProperty(
        required = true,
        value = ""
    )
    @field:JsonProperty(
        "lastSavedAt",
        required = true
    ) val lastSavedAt: java.time.Instant,

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty("language", required = true) val language: LanguageDto
)
