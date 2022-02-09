package delta.codecharacter.server.params.game_entities

data class Attacker(
    val id: Int,
    val hp: Int,
    val range: Int,
    val attackPower: Int,
    val speed: Int,
    val price: Int,
)
