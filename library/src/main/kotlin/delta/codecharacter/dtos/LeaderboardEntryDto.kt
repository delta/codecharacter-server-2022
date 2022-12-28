package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.dtos.PublicUserDto
import delta.codecharacter.dtos.UserStatsDto
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.Valid
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

