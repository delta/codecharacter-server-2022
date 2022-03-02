package delta.codecharacter.server.params

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import delta.codecharacter.server.params.game_entities.Attacker
import delta.codecharacter.server.params.game_entities.Defender

data class GameParameters(
    @field:JsonProperty("attackers", required = true) val attackers: Set<Attacker>,
    @field:JsonProperty("defenders", required = true) val defenders: Set<Defender>,
    @field:JsonProperty("no_of_turns", required = true) val numberOfTurns: Int,
    @field:JsonProperty("no_of_coins", required = true) val numberOfCoins: Int,
    @JsonIgnore val mapCoins: Int,
)
