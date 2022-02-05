package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.Valid

/**
 * Leaderboard entry model
 * @param user
 * @param stats
 */
data class LeaderboardEntryDto(

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty("user", required = true) val user: PublicUserDto,

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty("stats", required = true) val stats: UserStatsDto
)
