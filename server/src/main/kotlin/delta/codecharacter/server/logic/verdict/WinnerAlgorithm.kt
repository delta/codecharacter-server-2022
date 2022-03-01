package delta.codecharacter.server.logic.verdict

import delta.codecharacter.server.config.GameConfiguration
import delta.codecharacter.server.match.MatchVerdictEnum

class WinnerAlgorithm : VerdictAlgorithm {
    override fun getVerdict(
        player1HasErrors: Boolean,
        player1CoinsUsed: Int,
        player1Destruction: Double,
        player2HasErrors: Boolean,
        player2CoinsUsed: Int,
        player2Destruction: Double,
    ): MatchVerdictEnum {
        if (player1HasErrors && player2HasErrors) return MatchVerdictEnum.TIE
        if (player1HasErrors) return MatchVerdictEnum.PLAYER2
        if (player2HasErrors) return MatchVerdictEnum.PLAYER1

        val gameConfiguration = GameConfiguration()

        val totalCoins = gameConfiguration.gameParameters().numberOfCoins

        val player1CoinLeftPercentage = ((totalCoins - player1CoinsUsed.toDouble()) / totalCoins) * 100
        val player2CoinLeftPercentage = ((totalCoins - player2CoinsUsed.toDouble()) / totalCoins) * 100

        val scoreCalculator =
            { coinsLeftPercent: Double, destructionPercent: Double,
                ->
                coinsLeftPercent + (2 * destructionPercent)
            }

        val player1Score = scoreCalculator(player1CoinLeftPercentage, player1Destruction)
        val player2Score = scoreCalculator(player2CoinLeftPercentage, player2Destruction)

        if (player1Score == player2Score) return MatchVerdictEnum.TIE
        if (player1Score > player2Score) return MatchVerdictEnum.PLAYER1
        return MatchVerdictEnum.PLAYER2
    }
}
