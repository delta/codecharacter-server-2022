package delta.codecharacter.server.config

import delta.codecharacter.server.logic.daily_challenge_score.DailyChallengeScoreAlgorithm
import delta.codecharacter.server.logic.rating.GlickoRatingAlgorithm
import delta.codecharacter.server.logic.rating.RatingAlgorithm
import delta.codecharacter.server.logic.validation.MapValidator
import delta.codecharacter.server.logic.verdict.VerdictAlgorithm
import delta.codecharacter.server.logic.verdict.WinnerAlgorithm
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LogicConfiguration {

    @Bean
    fun ratingAlgorithm(): RatingAlgorithm {
        return GlickoRatingAlgorithm()
    }

    @Bean
    fun verdictAlgorithm(): VerdictAlgorithm {
        return WinnerAlgorithm()
    }

    @Bean
    fun mapValidator(): MapValidator {
        return MapValidator()
    }

    @Bean
    fun dailyChallengeScoreAlgorithm(): DailyChallengeScoreAlgorithm {
        return DailyChallengeScoreAlgorithm(gameConfiguration = GameConfiguration())
    }
}
