package delta.codecharacter.server.params

import delta.codecharacter.server.params.game_entities.Attacker
import delta.codecharacter.server.params.game_entities.Defender
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GameConfiguration {

    @Bean
    fun gameParameters(): GameParameters {
        return GameParameters(
            attackers =
            setOf(
                Attacker(id = 1, hp = 10, range = 3, attackPower = 3, speed = 3, price = 1),
                Attacker(id = 2, hp = 10, range = 3, attackPower = 3, speed = 3, price = 1),
            ),
            defenders =
            setOf(
                Defender(id = 1, hp = 10, range = 4, attackPower = 5, price = 1),
                Defender(id = 2, hp = 10, range = 6, attackPower = 5, price = 1),
            ),
            numberOfTurns = 500,
            numberOfCoins = 1000,
        )
    }
}
