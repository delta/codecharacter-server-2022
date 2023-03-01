package delta.codecharacter.server.user.public_user

import delta.codecharacter.dtos.CurrentUserProfileDto
import delta.codecharacter.dtos.DailyChallengeLeaderBoardResponseDto
import delta.codecharacter.dtos.LeaderboardEntryDto
import delta.codecharacter.dtos.PublicUserDto
import delta.codecharacter.dtos.TierTypeDto
import delta.codecharacter.dtos.TutorialUpdateTypeDto
import delta.codecharacter.dtos.UpdateCurrentUserProfileDto
import delta.codecharacter.dtos.UserStatsDto
import delta.codecharacter.server.daily_challenge.DailyChallengeEntity
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.match.MatchVerdictEnum
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID

@Service
class PublicUserService(@Autowired private val publicUserRepository: PublicUserRepository) {

    @Value("\${environment.no-of-tutorial-level}") private lateinit var totalTutorialLevels: Number
    @Value("\${environment.no-of-tier-1-players}") private var tier1Players: Number = 1
    @Value("\${environment.no-of-players-for-promotion}") private var topPlayer: Number = 1
    private val logger: Logger = LoggerFactory.getLogger(PublicUserService::class.java)

    fun create(
        userId: UUID,
        username: String,
        name: String,
        country: String,
        college: String,
        avatarId: Int
    ) {
        val publicUser =
            PublicUserEntity(
                userId = userId,
                username = username,
                name = name,
                country = country,
                college = college,
                avatarId = avatarId,
                rating = 1500.0,
                wins = 0,
                losses = 0,
                ties = 0,
                score = 0.0,
                tier = TierTypeDto.TIER_PRACTICE,
                tutorialLevel = 1,
                dailyChallengeHistory = HashMap()
            )
        publicUserRepository.save(publicUser)
    }

    fun updateLeaderboardAfterPracticePhase() {
        val publicUsers = publicUserRepository.findAll()
        publicUsers.forEachIndexed { index, user ->
            if (index < tier1Players.toInt()) {
                publicUserRepository.save(user.copy(tier = TierTypeDto.TIER1))
            } else {
                publicUserRepository.save(user.copy(tier = TierTypeDto.TIER2))
            }
        }
        logger.info("Leaderboard tier set during the start of game phase")
    }

    fun resetRatingsAfterPracticePhase() {
        logger.info("Reset ratings after practice phase starts")
        val users = publicUserRepository.findAll()
        users.forEach { user ->
            publicUserRepository.save(user.copy(rating = 1500.0, wins = 0, ties = 0, losses = 0))
        }
        logger.info("Reset ratings after practice phase has ended")
    }

    fun promoteTiers() {
        val topPlayersInTier2 =
            publicUserRepository.findAllByTier(
                TierTypeDto.TIER2,
                PageRequest.of(0, topPlayer.toInt(), Sort.by(Sort.Order.desc("rating")))
            )
        val bottomPlayersInTier1 =
            publicUserRepository.findAllByTier(
                TierTypeDto.TIER1,
                PageRequest.of(0, topPlayer.toInt(), Sort.by(Sort.Order.asc("rating")))
            )
        topPlayersInTier2.forEach { users ->
            val updatedToTier1User = publicUserRepository.save(users.copy(tier = TierTypeDto.TIER1))
            if (updatedToTier1User.tier == TierTypeDto.TIER1) {
                logger.info("UserName ${updatedToTier1User.username} got promoted to TIER1")
            } else {
                logger.error(
                    "Error occurred while updating ${updatedToTier1User.username} (UserName) to TIER1"
                )
            }
        }
        bottomPlayersInTier1.forEach { users ->
            val updateToTier2User = publicUserRepository.save(users.copy(tier = TierTypeDto.TIER2))
            if (updateToTier2User.tier == TierTypeDto.TIER2) {
                logger.info("UserName ${updateToTier2User.username} got demoted to TIER2")
            } else {
                logger.error(
                    "Error occurred while updating ${updateToTier2User.username} (UserName) to TIER2"
                )
            }
        }
    }

    fun getLeaderboard(page: Int?, size: Int?, tier: TierTypeDto?): List<LeaderboardEntryDto> {
        val pageRequest = PageRequest.of(page ?: 0, size ?: 10, Sort.by(Sort.Order.desc("rating")))
        val publicUsers =
            if (tier == null) {
                publicUserRepository.findAll(pageRequest).content
            } else {
                publicUserRepository.findAllByTier(tier, pageRequest)
            }
        return publicUsers.map {
            LeaderboardEntryDto(
                user =
                PublicUserDto(
                    username = it.username,
                    name = it.name,
                    tier = TierTypeDto.valueOf(it.tier.name),
                    country = it.country,
                    college = it.college,
                    avatarId = it.avatarId,
                ),
                stats =
                UserStatsDto(
                    rating = BigDecimal(it.rating),
                    wins = it.wins,
                    losses = it.losses,
                    ties = it.ties
                ),
            )
        }
    }

    fun getDailyChallengeLeaderboard(
        page: Int?,
        size: Int?
    ): List<DailyChallengeLeaderBoardResponseDto> {
        val pageRequest = PageRequest.of(page ?: 0, size ?: 10, Sort.by(Sort.Direction.DESC, "score"))
        return publicUserRepository.findAll(pageRequest).content.map {
            DailyChallengeLeaderBoardResponseDto(
                userName = it.username, score = BigDecimal(it.score), avatarId = it.avatarId
            )
        }
    }

    fun getUserProfile(userId: UUID, email: String): CurrentUserProfileDto {
        val user = publicUserRepository.findById(userId).get()
        return CurrentUserProfileDto(
            id = userId,
            username = user.username,
            email = email,
            name = user.name,
            country = user.country,
            college = user.college,
            tutorialLevel = user.tutorialLevel,
            avatarId = user.avatarId,
            isTutorialComplete = user.tutorialLevel == totalTutorialLevels.toInt()
        )
    }

    fun updateUserProfile(userId: UUID, updateCurrentUserProfileDto: UpdateCurrentUserProfileDto) {
        val user = publicUserRepository.findById(userId).get()

        if (updateCurrentUserProfileDto.name != null &&
            updateCurrentUserProfileDto.name!!.trim().length < 5
        ) {
            throw CustomException(HttpStatus.BAD_REQUEST, "Name must be minimum 5 characters")
        }

        if (updateCurrentUserProfileDto.country != null &&
            updateCurrentUserProfileDto.country!!.trim().isEmpty()
        ) {
            throw CustomException(HttpStatus.BAD_REQUEST, "Country can not be an empty")
        }

        if (updateCurrentUserProfileDto.college != null &&
            updateCurrentUserProfileDto.college!!.trim().isEmpty()
        ) {
            throw CustomException(HttpStatus.BAD_REQUEST, "College can not be an empty")
        }
        if (updateCurrentUserProfileDto.avatarId != null &&
            updateCurrentUserProfileDto.avatarId!! !in 0..19
        ) {
            throw CustomException(HttpStatus.BAD_REQUEST, "Selected Avatar is invalid")
        }

        val updatedUser =
            user.copy(
                name = updateCurrentUserProfileDto.name ?: user.name,
                country = updateCurrentUserProfileDto.country ?: user.country,
                college = updateCurrentUserProfileDto.college ?: user.college,
                avatarId = updateCurrentUserProfileDto.avatarId ?: user.avatarId,
                tutorialLevel =
                updateTutorialLevel(
                    updateCurrentUserProfileDto.updateTutorialLevel, user.tutorialLevel
                )
            )
        publicUserRepository.save(updatedUser)
    }

    fun updateTutorialLevel(updateTutorialType: TutorialUpdateTypeDto?, tutorialLevel: Int): Int {
        var updatedTutorialLevel = tutorialLevel
        when (updateTutorialType) {
            TutorialUpdateTypeDto.NEXT -> {
                if (tutorialLevel >= totalTutorialLevels.toInt()) {
                    return tutorialLevel
                }
                updatedTutorialLevel += 1
            }
            TutorialUpdateTypeDto.PREVIOUS -> {
                if (tutorialLevel <= 1) {
                    return 1
                }
                updatedTutorialLevel -= 1
            }
            TutorialUpdateTypeDto.SKIP -> {
                updatedTutorialLevel = totalTutorialLevels.toInt()
            }
            else -> {
                return tutorialLevel
            }
        }
        return updatedTutorialLevel
    }

    fun updatePublicRating(
        userId: UUID,
        isInitiator: Boolean,
        verdict: MatchVerdictEnum,
        newRating: Double
    ) {
        val user = publicUserRepository.findById(userId).get()
        val updatedUser =
            user.copy(
                rating = newRating,
                wins =
                if ((isInitiator && verdict == MatchVerdictEnum.PLAYER1) ||
                    (!isInitiator && verdict == MatchVerdictEnum.PLAYER2)
                )
                    user.wins + 1
                else user.wins,
                losses =
                if ((isInitiator && verdict == MatchVerdictEnum.PLAYER2) ||
                    (!isInitiator && verdict == MatchVerdictEnum.PLAYER1)
                )
                    user.losses + 1
                else user.losses,
                ties = if (verdict == MatchVerdictEnum.TIE) user.ties + 1 else user.ties
            )
        publicUserRepository.save(updatedUser)
    }

    fun getPublicUser(userId: UUID): PublicUserEntity {
        return publicUserRepository.findById(userId).get()
    }

    fun getPublicUserByUsername(username: String): PublicUserEntity {
        return publicUserRepository.findByUsername(username).orElseThrow {
            CustomException(HttpStatus.BAD_REQUEST, "Invalid username")
        }
    }

    fun isUsernameUnique(username: String): Boolean {
        return publicUserRepository.findByUsername(username).isEmpty
    }

    fun updateDailyChallengeScore(userId: UUID, score: Double, dailyChallenge: DailyChallengeEntity) {
        val user = publicUserRepository.findById(userId).get()
        val current = user.dailyChallengeHistory
        current[dailyChallenge.day] = DailyChallengeHistory(score, dailyChallenge)
        val updatedUser = user.copy(score = user.score + score, dailyChallengeHistory = current)
        publicUserRepository.save(updatedUser)
    }
}
