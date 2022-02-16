package delta.codecharacter.server.logic.rating

interface RatingAlgorithm {
    fun calculateNewRating(
        rating: Rating,
        opponentRatings: List<Rating>,
        opponentOutcomes: List<Double>,
    ): Rating
}
