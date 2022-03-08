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
        verdict: MatchVerdictEnum,
        isUserInTop15: Boolean,
        isOpponentInTop15: Boolean
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
                rating = if (isUserInTop15) userRating else newUserRating.rating,
                ratingDeviation = newUserRating.ratingDeviation,
                validFrom = currentInstant
            )
        )
        ratingHistoryRepository.save(
            RatingHistoryEntity(
                userId = opponentId,
                rating = if (isOpponentInTop15) opponentRating else newOpponentRating.rating,
                ratingDeviation = newOpponentRating.ratingDeviation,
                validFrom = currentInstant
            )
        )

        return Pair(
            if (isUserInTop15) userRating else newUserRating.rating,
            if (isOpponentInTop15) opponentRating else newOpponentRating.rating
        )
    }

    private fun getNewRatingAfterAutoMatches(
        userId: UUID,
        userRatingEntities: List<RatingHistoryEntity>,
        autoMatches: List<MatchEntity>
    ): GlickoRating {
        val userAsInitiatorMatches = autoMatches.filter { it.player1.userId == userId }
        val userAsOpponentMatches = autoMatches.filter { it.player2.userId == userId }

        val usersWeightedRatingDeviations =
            userRatingEntities.map {
                ratingAlgorithm.getWeightedRatingDeviationSinceLastCompetition(
                    it.ratingDeviation, it.validFrom
                )
            }

        val ratingsForUserAsInitiator =
            userAsInitiatorMatches.map { match ->
                GlickoRating(
                    match.player2.rating,
                    usersWeightedRatingDeviations[
                        userRatingEntities.indexOfFirst { ratingEntity ->
                            ratingEntity.userId == match.player2.userId
                        }
                    ]
                )
            }
        val verdictsForUserAsInitiator =
            userAsInitiatorMatches.map { match -> convertVerdictToMatchResult(match.verdict) }

        val ratingsForUserAsOpponent =
            userAsOpponentMatches.map { match ->
                GlickoRating(
                    match.player1.rating,
                    usersWeightedRatingDeviations[
                        userRatingEntities.indexOfFirst { ratingEntity ->
                            ratingEntity.userId == match.player1.userId
                        }
                    ]
                )
            }
        val verdictsForUserAsOpponent =
            userAsOpponentMatches.map { match -> 1.0 - convertVerdictToMatchResult(match.verdict) }

        val ratings = ratingsForUserAsInitiator + ratingsForUserAsOpponent
        val verdicts = verdictsForUserAsInitiator + verdictsForUserAsOpponent

        return ratingAlgorithm.calculateNewRating(
            GlickoRating(
                userRatingEntities.find { it.userId == userId }!!.rating,
                usersWeightedRatingDeviations[userRatingEntities.indexOfFirst { it.userId == userId }]
            ),
            ratings,
            verdicts
        )
    }

    fun updateAutoMatchRatings(userIds: List<UUID>, matches: List<MatchEntity>) {
        val ratingHistoryEntities =
            ratingHistoryRepository.findFirstByUserIdInOrderByValidFromDesc(userIds)
        val newRatings =
            userIds.map { userId ->
                val userRatingEntities = ratingHistoryEntities.filter { it.userId == userId }
                getNewRatingAfterAutoMatches(userId, userRatingEntities, matches)
            }
        val currentInstant = Instant.now()
        newRatings.forEachIndexed { index, rating ->
            ratingHistoryRepository.save(
                RatingHistoryEntity(
                    userId = userIds[index],
                    rating = rating.rating,
                    ratingDeviation = rating.ratingDeviation,
                    validFrom = currentInstant
                )
            )
        }
    }
}
