package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.annotation.JsonProperty
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
* Match/Game verdict
* Values: PLAYER1,PLAYER2,TIE
*/
enum class VerdictDto(val value: kotlin.String) {

    @JsonProperty("PLAYER1") PLAYER1("PLAYER1"),
    @JsonProperty("PLAYER2") PLAYER2("PLAYER2"),
    @JsonProperty("TIE") TIE("TIE")
}

