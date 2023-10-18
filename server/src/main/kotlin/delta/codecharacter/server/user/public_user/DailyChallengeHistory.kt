package delta.codecharacter.server.user.public_user

import delta.codecharacter.server.daily_challenge.DailyChallengeEntity

data class DailyChallengeHistory(val score: Double, val dailyChallenge: DailyChallengeEntity)
