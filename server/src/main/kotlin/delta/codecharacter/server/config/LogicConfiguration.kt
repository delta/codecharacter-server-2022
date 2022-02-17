package delta.codecharacter.server.config

import delta.codecharacter.server.logic.rating.RatingAlgorithm
import delta.codecharacter.server.logic.rating.ZeroRatingAlgorithm
import delta.codecharacter.server.logic.validation.MapValidator
import delta.codecharacter.server.logic.verdict.Player1WinnerAlgorithm
import delta.codecharacter.server.logic.verdict.VerdictAlgorithm
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LogicConfiguration {

    @Bean
    fun ratingAlgorithm(): RatingAlgorithm {
        return ZeroRatingAlgorithm()
    }

    @Bean
    fun verdictAlgorithm(): VerdictAlgorithm {
        return Player1WinnerAlgorithm()
    }

    @Bean
    fun mapValidator(): MapValidator {
        return MapValidator()
    }
}
