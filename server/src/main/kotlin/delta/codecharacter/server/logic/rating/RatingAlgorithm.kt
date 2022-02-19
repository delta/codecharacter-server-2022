package delta.codecharacter.server.logic.rating

import java.time.Instant

interface RatingAlgorithm {
    fun calculateNewRating(
        rating: GlickoRating,
        opponentRatings: List<GlickoRating>,
        opponentOutcomes: List<Double>,
    ): GlickoRating

    fun getWeightedRatingDeviationSinceLastCompetition(
        lastRD: Double,
        lastMatchDate: Instant,
    ): Double
}
