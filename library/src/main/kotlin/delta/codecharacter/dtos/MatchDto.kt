package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import javax.validation.Valid

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

    @ApiModelProperty(
        example = "123e4567-e89b-12d3-a456-426614174000",
        required = true,
        value = ""
    )
    @field:JsonProperty("id", required = true) val id: java.util.UUID,

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty(
        "games",
        required = true
    ) val games: Set<GameDto>,

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty(
        "matchMode",
        required = true
    ) val matchMode: MatchModeDto,

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty(
        "matchVerdict",
        required = true
    ) val matchVerdict: VerdictDto,

    @ApiModelProperty(
        required = true,
        value = ""
    )
    @field:JsonProperty(
        "createdAt",
        required = true
    ) val createdAt: java.time.Instant,

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty("user1", required = true) val user1: PublicUserDto,

    @field:Valid
    @ApiModelProperty(example = "null", required = true, value = "")
    @field:JsonProperty("user2", required = true) val user2: PublicUserDto
)
