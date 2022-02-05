package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.Valid

/**
 * Code revision model
 * @param id
 * @param code
 * @param language
 * @param createdAt
 * @param parentRevision
 */
data class CodeRevisionDto(

    @ApiModelProperty(
        example = "123e4567-e89b-12d3-a456-426614174000",
        required = true,
        value = ""
    )
    @field:JsonProperty("id", required = true) val id: java.util.UUID,

    @ApiModelProperty(
        example = "#include <iostream>",
        required = true,
        value = ""
    )
    @field:JsonProperty("code", required = true) val code: String,

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty("language", required = true) val language: LanguageDto,

    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty(
        "createdAt",
        required = true
    ) val createdAt: java.time.Instant,

    @ApiModelProperty(
        example = "123e4567-e89b-12d3-a456-426614174111",
        value = ""
    )
    @field:JsonProperty("parentRevision") val parentRevision: java.util.UUID? = null
)
