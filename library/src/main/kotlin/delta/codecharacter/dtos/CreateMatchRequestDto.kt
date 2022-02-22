package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.Valid

/**
 * Create match request  If mode is SELF: either/both of mapRevisionId and codeRevisionId have to be provided, or else latest code will be used to initiate the match If mode is MANUAL: only opponentUsername should be provided
 * @param mode
 * @param opponentUsername Username of the opponent
 * @param mapRevisionId Revision ID of the map
 * @param codeRevisionId Revision of the code
 */
data class CreateMatchRequestDto(

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty("mode", required = true) val mode: MatchModeDto,

    @ApiModelProperty(example = "null", value = "Username of the opponent")
    @field:JsonProperty("opponentUsername") val opponentUsername: String? = null,

    @ApiModelProperty(example = "null", value = "Revision ID of the map")
    @field:JsonProperty("mapRevisionId") val mapRevisionId: java.util.UUID? = null,

    @ApiModelProperty(example = "null", value = "Revision of the code")
    @field:JsonProperty("codeRevisionId") val codeRevisionId: java.util.UUID? = null
)
