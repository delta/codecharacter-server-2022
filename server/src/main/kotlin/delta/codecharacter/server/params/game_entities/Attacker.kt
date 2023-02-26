package delta.codecharacter.server.params.game_entities

import com.fasterxml.jackson.annotation.JsonProperty

data class Attacker(
    @field:JsonProperty("id", required = true) val id: Int,
    @field:JsonProperty("hp", required = true) val hp: Int,
    @field:JsonProperty("range", required = true) val range: Int,
    @field:JsonProperty("attack_power", required = true) val attackPower: Int,
    @field:JsonProperty("speed", required = true) val speed: Int,
    @field:JsonProperty("price", required = true) val price: Int,
    @field:JsonProperty("is_aerial", required = true) val aerial: Int,
)
