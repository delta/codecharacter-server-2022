package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Match/Game verdict
 * Values: PLAYER1,PLAYER2,TIE
 */
enum class VerdictDto(val value: String) {

    @JsonProperty("PLAYER1")
    PLAYER1("PLAYER1"),

    @JsonProperty("PLAYER2")
    PLAYER2("PLAYER2"),

    @JsonProperty("TIE")
    TIE("TIE");
}
