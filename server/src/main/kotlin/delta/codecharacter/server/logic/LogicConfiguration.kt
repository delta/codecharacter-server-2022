package delta.codecharacter.server.logic

import delta.codecharacter.server.logic.rating.RatingAlgorithm
import delta.codecharacter.server.logic.rating.ZeroRatingAlgorithm
import delta.codecharacter.server.logic.validation.MapValidator
import delta.codecharacter.server.logic.verdict.VerdictAlgorithm
import delta.codecharacter.server.logic.verdict.WinnerAlgorithm
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
        return WinnerAlgorithm()
    }

    @Bean
    fun mapValidator(): MapValidator {
        return MapValidator()
    }
}
