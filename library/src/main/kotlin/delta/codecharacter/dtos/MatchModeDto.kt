package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
* Match Mode
* Values: SELF,MANUAL,AUTO
*/
enum class MatchModeDto(val value: kotlin.String) {

    @JsonProperty("SELF") SELF("SELF"),
    @JsonProperty("MANUAL") MANUAL("MANUAL"),
    @JsonProperty("AUTO") AUTO("AUTO")
}

