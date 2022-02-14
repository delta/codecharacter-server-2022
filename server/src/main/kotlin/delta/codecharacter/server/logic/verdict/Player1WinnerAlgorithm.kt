package delta.codecharacter.server.logic.verdict

import delta.codecharacter.server.match.MatchVerdictEnum

class Player1WinnerAlgorithm : VerdictAlgorithm {
    override fun getVerdict(
        player1HasErrors: Boolean,
        player1CoinsUsed: Int,
        player1Destruction: Double,
        player2HasErrors: Boolean,
        player2CoinsUsed: Int,
        player2Destruction: Double
    ): MatchVerdictEnum {
        return MatchVerdictEnum.PLAYER1
    }
}
