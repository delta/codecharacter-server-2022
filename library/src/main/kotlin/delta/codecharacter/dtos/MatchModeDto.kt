package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Match Mode
 * Values: SELF,MANUAL,AUTO
 */
enum class MatchModeDto(val value: String) {

    @JsonProperty("SELF")
    SELF("SELF"),

    @JsonProperty("MANUAL")
    MANUAL("MANUAL"),

    @JsonProperty("AUTO")
    AUTO("AUTO");
}
