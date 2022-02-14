package delta.codecharacter.server.logic.rating

interface RatingAlgorithm {
    fun calculateRating(currentRating: Int): Int
}
