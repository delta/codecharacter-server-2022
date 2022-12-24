package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.annotation.JsonProperty
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
* Match/Game verdict
* Values: PLAYER1,PLAYER2,TIE
*/
enum class VerdictDto(val value: kotlin.String) {

    @JsonProperty("PLAYER1") PLAYER1("PLAYER1"),
    @JsonProperty("PLAYER2") PLAYER2("PLAYER2"),
    @JsonProperty("TIE") TIE("TIE")
}

