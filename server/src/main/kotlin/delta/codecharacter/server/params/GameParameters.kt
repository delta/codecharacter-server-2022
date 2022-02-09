package delta.codecharacter.server.params

import delta.codecharacter.server.params.game_entities.Attacker
import delta.codecharacter.server.params.game_entities.Defender

data class GameParameters(
    val attackers: Set<Attacker>,
    val defenders: Set<Defender>,
    val numberOfTurns: Int,
    val numberOfCoins: Int,
)
