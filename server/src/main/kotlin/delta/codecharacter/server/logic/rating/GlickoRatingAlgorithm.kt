package delta.codecharacter.server.logic.rating

import java.time.Instant
import java.util.concurrent.TimeUnit
import kotlin.math.PI
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

/*
   Refer: http://www.glicko.net/glicko/glicko.pdf
*/
class GlickoRatingAlgorithm : RatingAlgorithm {

    // Constant
    private val q: Double = ln(10.0) / 400

    // " One practical problem with the Glicko system is that when a player competes very frequently,
    // his/her rating stops changing appreciably which reflects that the RD is very small. This may
    // sometimes prevent a player’s rating from changing substantially when the player is truly
    // improving. I would therefore recommend that an RD never drop below a threshold value,
    // such as 30, so that ratings can change appreciably even in a relatively short time."
    //                          - Mark E Glickman
    private val minRatingDeviation: Double = 30.0

    // Amount which decides how much a player's rating deviation changes every time period
    private val c: Double = 416.0 // last year's value change this lol

    // Time for one rating period in minutes
    private val timePeriod: Double = 5.0

    // Wont let RD be more than this value
    private val unratedPlayerRD: Double = 350.0

    private fun g(rd: Double): Double = 1.0 / (sqrt(1.0 + ((3.0 * q * q * rd * rd)) / (PI * PI)))

    private fun e(r0: Double, rI: Double, rdI: Double): Double {
        val grdI = g(rdI)
        val ratingDiff = r0 - rI
        return 1.0 / (1.0 + ((10.0).pow((grdI * ratingDiff) / (-400.0))))
    }

    private fun dSquared(
        r0: Double,
        opponentRatings: List<Rating>,
    ): Double {
        var sm = 0.0
        for (oppRating in opponentRatings) {
            val rI = oppRating.getRating()
            val rdI = oppRating.getRatingDeviation()
            val grd = g(rdI)
            val expectation = e(r0, rI, rdI)
            sm += (grd * grd * expectation * (1.0 - expectation))
        }
        return 1.0 / (q * q * sm)
    }

    override fun calculateNewRating(
        rating: Rating,
        opponentRatings: List<Rating>,
        opponentOutcomes: List<Double>,
    ): Rating {
        val r0 = rating.getRating()
        var sm = 0.0
        for ((i, opponentRating) in opponentRatings.withIndex()) {
            val rI = opponentRating.getRating()
            val rdI = opponentRating.getRatingDeviation()
            val grd = g(rdI)
            val expectation = e(r0, rI, rdI)
            val sI = opponentOutcomes[i]
            sm += (grd * (sI - expectation))
        }
        val rd = rating.getRatingDeviation()
        val d2 = dSquared(r0, opponentRatings)
        val numerator = q * sm
        val denominator = (1.0 / (rd * rd)) + (1.0 / d2)

        val newR = r0 + (numerator / denominator)
        val newRd = max(minRatingDeviation, sqrt(1.0 / ((1.0 / (rd * rd)) + (1.0 / d2))))

        return GlickoRating(newR, newRd)
    }

    fun getWeightedRatingDeviationSinceLastCompetetion(
        lastRD: Double,
        lastMatchDate: Instant,
    ): Double {
        val currentInstant = Instant.now()
        val offset = currentInstant.toEpochMilli() - lastMatchDate.toEpochMilli()
        val noOfTimePeriodsElapsed = TimeUnit.MILLISECONDS.toMinutes(offset).toDouble() / timePeriod
        return min(unratedPlayerRD, sqrt((lastRD * lastRD) + (c * c * noOfTimePeriodsElapsed)))
    }
}
