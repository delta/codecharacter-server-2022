package delta.codecharacter.server.logic.rating

interface RatingAlgorithm {
    fun calculateNewRating(
        rating: GlickoRating,
        opponentRatings: List<GlickoRating>,
        opponentOutcomes: List<Double>,
    ): GlickoRating
}
