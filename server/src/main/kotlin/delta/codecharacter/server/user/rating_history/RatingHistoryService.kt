package delta.codecharacter.server.user.rating_history

import delta.codecharacter.dtos.RatingHistoryDto
import delta.codecharacter.server.logic.rating.GlickoRating
import delta.codecharacter.server.logic.rating.RatingAlgorithm
import delta.codecharacter.server.match.MatchEntity
import delta.codecharacter.server.match.MatchVerdictEnum
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Service
class RatingHistoryService(
    @Autowired private val ratingHistoryRepository: RatingHistoryRepository,
    @Autowired private val ratingAlgorithm: RatingAlgorithm
) {
    fun create(userId: UUID) {
        val ratingHistory =
            RatingHistoryEntity(
                userId = userId, rating = 1500.0, ratingDeviation = 350.0, validFrom = Instant.now()
            )
        ratingHistoryRepository.save(ratingHistory)
    }

    fun getRatingHistory(userId: UUID): List<RatingHistoryDto> {
        return ratingHistoryRepository.findAllByUserId(userId).map {
            RatingHistoryDto(
                rating = BigDecimal(it.rating),
                ratingDeviation = BigDecimal(it.ratingDeviation),
                validFrom = it.validFrom
            )
        }
    }

    private fun convertVerdictToMatchResult(verdict: MatchVerdictEnum): Double {
        return when (verdict) {
            MatchVerdictEnum.PLAYER1 -> 1.0
            MatchVerdictEnum.PLAYER2 -> 0.0
            MatchVerdictEnum.TIE -> 0.5
        }
    }

    fun updateRating(
        userId: UUID,
        opponentId: UUID,
        verdict: MatchVerdictEnum
    ): Pair<Double, Double> {
        val (_, userRating, userRatingDeviation, userRatingValidFrom) =
            ratingHistoryRepository.findFirstByUserIdOrderByValidFromDesc(userId)
        val (_, opponentRating, opponentRatingDeviation, opponentRatingValidFrom) =
            ratingHistoryRepository.findFirstByUserIdOrderByValidFromDesc(opponentId)

        val userWeightedRatingDeviation =
            ratingAlgorithm.getWeightedRatingDeviationSinceLastCompetition(
                userRatingDeviation, userRatingValidFrom
            )
        val opponentWeightedRatingDeviation =
            ratingAlgorithm.getWeightedRatingDeviationSinceLastCompetition(
                opponentRatingDeviation, opponentRatingValidFrom
            )

        val newUserRating =
            ratingAlgorithm.calculateNewRating(
                GlickoRating(userRating, userWeightedRatingDeviation),
                listOf(GlickoRating(opponentRating, opponentWeightedRatingDeviation)),
                listOf(convertVerdictToMatchResult(verdict))
            )
        val newOpponentRating =
            ratingAlgorithm.calculateNewRating(
                GlickoRating(opponentRating, opponentWeightedRatingDeviation),
                listOf(GlickoRating(userRating, userWeightedRatingDeviation)),
                listOf(1.0 - convertVerdictToMatchResult(verdict))
            )

        val currentInstant = Instant.now()
        ratingHistoryRepository.save(
            RatingHistoryEntity(
                userId = userId,
                rating = newUserRating.rating,
                ratingDeviation = newUserRating.ratingDeviation,
                validFrom = currentInstant
            )
        )
        ratingHistoryRepository.save(
            RatingHistoryEntity(
                userId = opponentId,
                rating = newOpponentRating.rating,
                ratingDeviation = newOpponentRating.ratingDeviation,
                validFrom = currentInstant
            )
        )

        return Pair(newUserRating.rating, newOpponentRating.rating)
    }

    private fun getNewRatingAfterAutoMatches(
        userId: UUID,
        userRatings: Map<UUID, RatingHistoryEntity>,
        autoMatches: List<MatchEntity>
    ): GlickoRating {
        val userAsInitiatorMatches = autoMatches.filter { it.player1.userId == userId }
        val userAsOpponentMatches = autoMatches.filter { it.player2.userId == userId }

        val usersWeightedRatingDeviations =
            userRatings
                .map {
                    it.key to
                        ratingAlgorithm.getWeightedRatingDeviationSinceLastCompetition(
                            it.value.ratingDeviation, it.value.validFrom
                        )
                }
                .toMap()

        val ratingsForUserAsPlayer1 =
            userAsInitiatorMatches.map { match ->
                GlickoRating(match.player2.rating, usersWeightedRatingDeviations[match.player2.userId]!!)
            }
        val verdictsForUserAsPlayer1 =
            userAsInitiatorMatches.map { match -> convertVerdictToMatchResult(match.verdict) }

        val ratingsForUserAsPlayer2 =
            userAsOpponentMatches.map { match ->
                GlickoRating(match.player1.rating, usersWeightedRatingDeviations[match.player1.userId]!!)
            }
        val verdictsForUserAsPlayer2 =
            userAsOpponentMatches.map { match -> 1.0 - convertVerdictToMatchResult(match.verdict) }

        val ratings = ratingsForUserAsPlayer1 + ratingsForUserAsPlayer2
        val verdicts = verdictsForUserAsPlayer1 + verdictsForUserAsPlayer2

        return ratingAlgorithm.calculateNewRating(
            GlickoRating(userRatings[userId]!!.rating, usersWeightedRatingDeviations[userId]!!),
            ratings,
            verdicts
        )
    }

    fun updateAndGetAutoMatchRatings(
        userIds: List<UUID>,
        matches: List<MatchEntity>
    ): Map<UUID, GlickoRating> {
        val userRatings =
            userIds.associateWith { userId ->
                ratingHistoryRepository.findFirstByUserIdOrderByValidFromDesc(userId)
            }
        val newRatings =
            userIds.associateWith { userId ->
                getNewRatingAfterAutoMatches(userId, userRatings, matches)
            }
        val currentInstant = Instant.now()
        newRatings.forEach { (userId, rating) ->
            ratingHistoryRepository.save(
                RatingHistoryEntity(
                    userId = userId,
                    rating = rating.rating,
                    ratingDeviation = rating.ratingDeviation,
                    validFrom = currentInstant
                )
            )
        }

        return newRatings
    }
}
