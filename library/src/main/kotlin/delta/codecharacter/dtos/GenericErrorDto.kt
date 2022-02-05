package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty

/**
 * Model for Generic Error
 * @param message
 */
data class GenericErrorDto(

    @ApiModelProperty(example = "null", value = "")
    @field:JsonProperty("message") val message: String? = null
)
