package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.dtos.MatchModeDto
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
 * Create match request  If mode is SELF: either/both of mapRevisionId and codeRevisionId have to be provided, or else latest code will be used to initiate the match If mode is MANUAL: only opponentUsername should be provided
 * @param mode 
 * @param opponentUsername Username of the opponent
 * @param mapRevisionId Revision ID of the map
 * @param codeRevisionId Revision of the code
 */
data class CreateMatchRequestDto(

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("mode", required = true) val mode: MatchModeDto,

    @Schema(example = "null", description = "Username of the opponent")
    @field:JsonProperty("opponentUsername") val opponentUsername: kotlin.String? = null,

    @Schema(example = "null", description = "Revision ID of the map")
    @field:JsonProperty("mapRevisionId") val mapRevisionId: java.util.UUID? = null,

    @Schema(example = "null", description = "Revision of the code")
    @field:JsonProperty("codeRevisionId") val codeRevisionId: java.util.UUID? = null
) {

}

