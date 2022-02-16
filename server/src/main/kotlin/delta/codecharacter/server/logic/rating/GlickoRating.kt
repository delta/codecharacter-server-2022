package delta.codecharacter.server.logic.rating

class GlickoRating(
    private val rating: Double,
    private val ratingDeviation: Double,
) : Rating {
    override fun getRating(): Double = rating
    override fun getRatingDeviation(): Double = ratingDeviation
}
