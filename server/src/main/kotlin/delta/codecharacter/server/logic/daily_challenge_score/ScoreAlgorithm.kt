package delta.codecharacter.server.logic.daily_challenge_score

import delta.codecharacter.dtos.ChallengeTypeDto
import delta.codecharacter.server.daily_challenge.DailyChallengeEntity

interface ScoreAlgorithm {
    fun getDailyChallengeScore(
        playerCoinsUsed: Int,
        playerDestruction: Double,
        dailyChallenge: DailyChallengeEntity
    ): Double

    fun getPlayerBaseScore(
        coinsLeftPercent: Double,
        destructionPercent: Double,
        perfectBaseScore: Double,
        challType: ChallengeTypeDto
    ): Double

    fun getHoursSinceDailyChallengeLaunched(): Double

    fun getPlayerTimeScore(
        perfectTimeScore: Double,
    ): Double
}
