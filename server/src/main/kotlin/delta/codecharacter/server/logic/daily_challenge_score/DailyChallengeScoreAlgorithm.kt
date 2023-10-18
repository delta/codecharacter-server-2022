package delta.codecharacter.server.logic.daily_challenge_score

import delta.codecharacter.dtos.ChallengeTypeDto
import delta.codecharacter.server.config.GameConfiguration
import delta.codecharacter.server.daily_challenge.DailyChallengeEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import java.time.Duration
import java.time.Instant
import kotlin.math.exp

class DailyChallengeScoreAlgorithm(@Autowired private val gameConfiguration: GameConfiguration) :
    ScoreAlgorithm {

    @Value("\${environment.event-start-date}") private lateinit var startDate: String
    private val perfectBasePartConstant = 0.7
    private val perfectTimePartConstant = 0.3
    private val exponentialConstantForBasePart = 150
    private val exponentialConstantForTimePart = 15
    private val secondsInADay = 86400
    private val secondsInAnHour = 3600

    override fun getHoursSinceDailyChallengeLaunched(): Double {
        val givenDateTime = Instant.parse(startDate)
        val nowDateTime = Instant.now()
        val period: Duration = Duration.between(givenDateTime, nowDateTime)

        return (period.toSeconds().toDouble().rem(secondsInADay)) / secondsInAnHour
    }

    override fun getPlayerBaseScore(
        coinsLeftPercent: Double,
        destructionPercent: Double,
        perfectBaseScore: Double,
        challType: ChallengeTypeDto
    ): Double {
        if (challType == ChallengeTypeDto.CODE)
            return ((100.0 - coinsLeftPercent) + (2 * (100 - destructionPercent)) + perfectBaseScore)
        return (coinsLeftPercent + (2 * destructionPercent) + perfectBaseScore)
    }

    override fun getPlayerTimeScore(perfectTimeScore: Double): Double {
        val hours = getHoursSinceDailyChallengeLaunched()
        return perfectTimeScore * exp((-1) * (hours / exponentialConstantForTimePart))
    }

    override fun getDailyChallengeScore(
        playerCoinsUsed: Int,
        playerDestruction: Double,
        dailyChallenge: DailyChallengeEntity
    ): Double {
        val totalCoins = gameConfiguration.gameParameters().numberOfCoins
        val (_, _, _, challType, _, _, perfectScore, numberOfCompletions) = dailyChallenge
        val perfectBasePart =
            perfectBasePartConstant *
                perfectScore *
                exp(((-1) * (numberOfCompletions.toDouble() / exponentialConstantForBasePart)))
        val perfectTimePart = perfectTimePartConstant * perfectScore
        val coinsLeftPercentage = ((totalCoins - playerCoinsUsed.toDouble()) / totalCoins) * 100
        return (
            (
                getPlayerBaseScore(
                    coinsLeftPercentage, playerDestruction, perfectBasePart, challType
                ) +
                    getPlayerTimeScore(perfectTimePart)
                ) * 100.0
            )
            .toInt() / 100.0
    }
}
