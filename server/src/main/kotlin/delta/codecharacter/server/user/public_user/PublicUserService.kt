package delta.codecharacter.server.user.public_user

import delta.codecharacter.dtos.CurrentUserProfileDto
import delta.codecharacter.dtos.LeaderboardEntryDto
import delta.codecharacter.dtos.PublicUserDto
import delta.codecharacter.dtos.UpdateCurrentUserProfileDto
import delta.codecharacter.dtos.UserStatsDto
import delta.codecharacter.server.exception.CustomException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.UUID

@Service
class PublicUserService(@Autowired private val publicUserRepository: PublicUserRepository) {

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
            )
        publicUserRepository.save(publicUser)
    }

    fun getLeaderboard(page: Int?, size: Int?): List<LeaderboardEntryDto> {
        val pageRequest = PageRequest.of(page ?: 0, size ?: 10, Sort.by(Sort.Direction.DESC, "rating"))
        return publicUserRepository.findAll(pageRequest).content.map {
            LeaderboardEntryDto(
                user =
                PublicUserDto(
                    username = it.username,
                    name = it.name,
                    country = it.country,
                    college = it.college,
                    avatarId = it.avatarId,
                ),
                stats =
                UserStatsDto(
                    rating = BigDecimal(it.rating),
                    wins = it.wins,
                    losses = it.losses,
                    ties = it.ties,
                )
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
            avatarId = user.avatarId,
        )
    }

    fun updateUserProfile(userId: UUID, updateCurrentUserProfileDto: UpdateCurrentUserProfileDto) {
        val user = publicUserRepository.findById(userId).get()
        val updatedUser =
            user.copy(
                name = updateCurrentUserProfileDto.name ?: user.name,
                country = updateCurrentUserProfileDto.country ?: user.country,
                college = updateCurrentUserProfileDto.college ?: user.college,
                avatarId = updateCurrentUserProfileDto.avatarId ?: user.avatarId
            )
        publicUserRepository.save(updatedUser)
    }

    fun updatePublicRating(userId: UUID, isWinner: Boolean, isTie: Boolean, newRating: Double) {
        val user = publicUserRepository.findById(userId).get()
        val updatedUser =
            user.copy(
                rating = newRating,
                wins = if (isWinner) user.wins + 1 else user.wins,
                losses = if (isWinner) user.losses else user.losses + 1,
                ties = if (isTie) user.ties + 1 else user.ties
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
}
