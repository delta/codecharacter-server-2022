package delta.codecharacter.server.logic.rating

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GlickoRatingAlgorithmTest {

    @BeforeEach fun setUp() {}

    @Test
    fun calculateNewRating() {
    /*
     * Refer to example of step 2: http://www.glicko.net/glicko/glicko.pdf
     */
        val playerRating = GlickoRating(1500.0, 200.0)
        val opponentRatings =
            listOf(GlickoRating(1400.0, 30.0), GlickoRating(1550.0, 100.0), GlickoRating(1700.0, 300.0))
        val outcomes = listOf(1.0, 0.0, 0.0)

        val ratingAlgorithm = GlickoRatingAlgorithm()

        val newPlayerRating =
            ratingAlgorithm.calculateNewRating(playerRating, opponentRatings, outcomes)

        assert((newPlayerRating.rating - 1464.0) <= 0.2)
        assert((newPlayerRating.ratingDeviation - 151.4) <= 0.2)
    }
}
