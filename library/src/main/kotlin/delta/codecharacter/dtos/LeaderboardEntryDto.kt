package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.dtos.PublicUserDto
import delta.codecharacter.dtos.UserStatsDto
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
 * Leaderboard entry model
 * @param user 
 * @param stats 
 */
data class LeaderboardEntryDto(

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("user", required = true) val user: PublicUserDto,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("stats", required = true) val stats: UserStatsDto
) {

}

