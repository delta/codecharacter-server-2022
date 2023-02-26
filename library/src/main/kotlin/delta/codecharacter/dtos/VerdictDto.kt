package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
* Match/Game verdict
* Values: PLAYER1,PLAYER2,TIE,SUCCESS,FAILURE
*/
enum class VerdictDto(val value: kotlin.String) {

    @JsonProperty("PLAYER1") PLAYER1("PLAYER1"),
    @JsonProperty("PLAYER2") PLAYER2("PLAYER2"),
    @JsonProperty("TIE") TIE("TIE"),
    @JsonProperty("SUCCESS") SUCCESS("SUCCESS"),
    @JsonProperty("FAILURE") FAILURE("FAILURE")
}

