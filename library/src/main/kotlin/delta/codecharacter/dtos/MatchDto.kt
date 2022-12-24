package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.dtos.GameDto
import delta.codecharacter.dtos.MatchModeDto
import delta.codecharacter.dtos.PublicUserDto
import delta.codecharacter.dtos.VerdictDto
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
 * Match model
 * @param id 
 * @param games 
 * @param matchMode 
 * @param matchVerdict 
 * @param createdAt 
 * @param user1 
 * @param user2 
 */
data class MatchDto(

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000", required = true, description = "")
    @field:JsonProperty("id", required = true) val id: java.util.UUID,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("games", required = true) val games: kotlin.collections.Set<GameDto>,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("matchMode", required = true) val matchMode: MatchModeDto,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("matchVerdict", required = true) val matchVerdict: VerdictDto,

    @Schema(example = "2021-01-01T00:00Z", required = true, description = "")
    @field:JsonProperty("createdAt", required = true) val createdAt: java.time.Instant,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("user1", required = true) val user1: PublicUserDto,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("user2", required = true) val user2: PublicUserDto
) {

}

