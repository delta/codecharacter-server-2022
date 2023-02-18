package delta.codecharacter.server.config

import delta.codecharacter.server.params.GameParameters
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
                Attacker(
                    id = 1,
                    hp = 10,
                    range = 2,
                    attackPower = 4,
                    speed = 4,
                    price = 2,
                    aerial = false,
                ),
                Attacker(
                    id = 2,
                    hp = 20,
                    range = 4,
                    attackPower = 2,
                    speed = 2,
                    price = 2,
                    aerial = false,
                ),
                Attacker(
                    id = 3,
                    hp = 15,
                    range = 6,
                    attackPower = 2,
                    speed = 6,
                    price = 4,
                    aerial = true,
                ),
            ),
            defenders =
            setOf(
                Defender(id = 1, hp = 400, range = 4, attackPower = 10, price = 25, aerial = false),
                Defender(id = 2, hp = 600, range = 6, attackPower = 20, price = 50, aerial = false),
                Defender(id = 3, hp = 400, range = 6, attackPower = 5, price = 75, aerial = true),
            ),
            numberOfTurns = 500,
            numberOfCoins = 1000,
        )
    }
}
