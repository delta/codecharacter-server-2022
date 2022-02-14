package delta.codecharacter.server.logic.rating

class ZeroRatingAlgorithm : RatingAlgorithm {
    override fun calculateRating(currentRating: Int): Int {
        return 0
    }
}
