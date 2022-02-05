package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.Valid

/**
 * Create match request
 * @param mode
 * @param opponentId User ID of the opponent
 * @param mapRevisionId Revision ID of the map
 * @param codeRevisionId Revision of the code
 */
data class CreateMatchRequestDto(

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty("mode", required = true) val mode: MatchModeDto,

    @ApiModelProperty(example = "null", value = "User ID of the opponent")
    @field:JsonProperty("opponentId") val opponentId: java.util.UUID? = null,

    @ApiModelProperty(example = "null", value = "Revision ID of the map")
    @field:JsonProperty("mapRevisionId") val mapRevisionId: java.util.UUID? = null,

    @ApiModelProperty(example = "null", value = "Revision of the code")
    @field:JsonProperty("codeRevisionId") val codeRevisionId: java.util.UUID? = null
)
