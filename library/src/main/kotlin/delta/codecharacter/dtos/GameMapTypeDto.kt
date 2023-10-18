package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
* 
* Values: NORMAL,DAILY_CHALLENGE
*/
enum class GameMapTypeDto(val value: kotlin.String) {

    @JsonProperty("NORMAL") NORMAL("NORMAL"),
    @JsonProperty("DAILY_CHALLENGE") DAILY_CHALLENGE("DAILY_CHALLENGE")
}

