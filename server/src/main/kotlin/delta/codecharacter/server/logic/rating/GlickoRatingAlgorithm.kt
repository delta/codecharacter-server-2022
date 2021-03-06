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

    // Time for one rating period in minutes
    private val timePeriod: Double = 15.0

    // Amount which decides how much a player's rating deviation changes every time period
    // Using timePeriod 15 mins and time to reduce the rating to min rating as 48 hrs,
    // we get the number of time periods to reduce the rating to min rating as 192.
    // Using these we calculate c = sqrt((350**2 - 50**2)/192) = 25, where 50 is
    // the rating deviation of a typical player.
    private val c: Double = 25.0

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
        opponentRatings: List<GlickoRating>,
    ): Double {
        var sm = 0.0
        for (oppRating in opponentRatings) {
            val rI = oppRating.rating
            val rdI = oppRating.ratingDeviation
            val grd = g(rdI)
            val expectation = e(r0, rI, rdI)
            sm += (grd * grd * expectation * (1.0 - expectation))
        }
        return 1.0 / (q * q * sm)
    }

    override fun calculateNewRating(
        rating: GlickoRating,
        opponentRatings: List<GlickoRating>,
        opponentOutcomes: List<Double>,
    ): GlickoRating {
        val r0 = rating.rating
        var sm = 0.0
        for ((i, opponentRating) in opponentRatings.withIndex()) {
            val rI = opponentRating.rating
            val rdI = opponentRating.ratingDeviation
            val grd = g(rdI)
            val expectation = e(r0, rI, rdI)
            val sI = opponentOutcomes[i]
            sm += (grd * (sI - expectation))
        }
        val rd = rating.ratingDeviation
        val d2 = dSquared(r0, opponentRatings)
        val numerator = q * sm
        val denominator = (1.0 / (rd * rd)) + (1.0 / d2)

        val newR = r0 + (numerator / denominator)
        val newRd = max(minRatingDeviation, sqrt(1.0 / ((1.0 / (rd * rd)) + (1.0 / d2))))

        return GlickoRating(newR, newRd)
    }

    override fun getWeightedRatingDeviationSinceLastCompetition(
        lastRD: Double,
        lastMatchDate: Instant,
    ): Double {
        val currentInstant = Instant.now()
        val offset = currentInstant.toEpochMilli() - lastMatchDate.toEpochMilli()
        val noOfTimePeriodsElapsed = TimeUnit.MILLISECONDS.toMinutes(offset).toDouble() / timePeriod
        return min(unratedPlayerRD, sqrt((lastRD * lastRD) + (c * c * noOfTimePeriodsElapsed)))
    }
}
