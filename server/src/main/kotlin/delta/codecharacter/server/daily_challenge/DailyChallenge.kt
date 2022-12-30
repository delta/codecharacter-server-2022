package delta.codecharacter.server.daily_challenge

data class DailyChallenge(
        val map: String?,
        val maxCoinsToUse: Int?,
        val defensesToBeDestroyed: Int?,
        val destructionPercentage: String?,
        val code: String?,
        val maxAirDefenses: Int?,
        val maxLandDefenses: Int?
)
