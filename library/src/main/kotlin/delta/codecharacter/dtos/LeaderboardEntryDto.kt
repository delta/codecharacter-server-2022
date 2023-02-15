package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.dtos.PublicUserDto
import delta.codecharacter.dtos.UserStatsDto
import jakarta.validation.constraints.*
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
    @get:JsonProperty("user", required = true) val user: PublicUserDto,

    @field:Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("stats", required = true) val stats: UserStatsDto
) {

}

