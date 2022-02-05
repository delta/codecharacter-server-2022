package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.Valid

/**
 * Update latest code request
 * @param code
 * @param language
 * @param lock
 */
data class UpdateLatestCodeRequestDto(

    @ApiModelProperty(
        example = "#include <iostream>",
        required = true,
        value = ""
    )
    @field:JsonProperty("code", required = true) val code: String,

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty("language", required = true) val language: LanguageDto,

    @ApiModelProperty(example = "null", value = "")
    @field:JsonProperty("lock") val lock: Boolean? = false
)
