package delta.codecharacter.server.leaderboard

import delta.codecharacter.dtos.TierTypeDto
import delta.codecharacter.server.TestAttributes
import delta.codecharacter.server.user.public_user.DailyChallengeHistory
import delta.codecharacter.server.user.public_user.PublicUserEntity
import delta.codecharacter.server.user.public_user.PublicUserRepository
import delta.codecharacter.server.user.public_user.PublicUserService
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

internal class LeaderboardTest {

    private lateinit var publicUserRepository: PublicUserRepository
    private lateinit var publicUserService: PublicUserService
    private lateinit var publicUserEntity: PublicUserEntity

    private var user1 =
        PublicUserEntity(
            userId = UUID.randomUUID(),
            username = "testUser1",
            name = "test user",
            country = "In",
            college = "college",
            avatarId = 1,
            tier = TierTypeDto.TIER1,
            tutorialLevel = 1,
            rating = 2000.0,
            wins = 0,
            losses = 0,
            ties = 0,
            isActivated = true,
            score = 0.0,
            dailyChallengeHistory =
            hashMapOf(0 to DailyChallengeHistory(0.0, TestAttributes.dailyChallengeCode)),
        )
    private var user2 =
        PublicUserEntity(
            userId = UUID.randomUUID(),
            username = "testUser2",
            name = "test user",
            country = "In",
            college = "college",
            avatarId = 1,
            tier = TierTypeDto.TIER1,
            tutorialLevel = 1,
            rating = 1800.0,
            wins = 0,
            losses = 0,
            ties = 0,
            isActivated = true,
            score = 0.0,
            dailyChallengeHistory =
            hashMapOf(0 to DailyChallengeHistory(0.0, TestAttributes.dailyChallengeCode)),
        )
    private var user3 =
        PublicUserEntity(
            userId = UUID.randomUUID(),
            username = "testUser3",
            name = "test user",
            country = "In",
            college = "college",
            avatarId = 1,
            tier = TierTypeDto.TIER2,
            tutorialLevel = 1,
            rating = 1600.0,
            wins = 0,
            losses = 0,
            ties = 0,
            isActivated = true,
            score = 0.0,
            dailyChallengeHistory =
            hashMapOf(0 to DailyChallengeHistory(0.0, TestAttributes.dailyChallengeCode)),
        )
    private var user4 =
        PublicUserEntity(
            userId = UUID.randomUUID(),
            username = "testUser4",
            name = "test user",
            country = "In",
            college = "college",
            avatarId = 1,
            tier = TierTypeDto.TIER2,
            tutorialLevel = 1,
            rating = 1500.0,
            wins = 0,
            losses = 0,
            ties = 0,
            isActivated = true,
            score = 0.0,
            dailyChallengeHistory =
            hashMapOf(0 to DailyChallengeHistory(0.0, TestAttributes.dailyChallengeCode)),
        )

    @BeforeEach
    fun setUp() {
        publicUserRepository = mockk(relaxed = true)
        publicUserService = PublicUserService(publicUserRepository)
        publicUserEntity = mockk(relaxed = true)
    }
    @Test
    fun `should get leaderboard by tiers`() {

        every { publicUserRepository.findAllByTier(TierTypeDto.TIER1, any()) } returns
            listOf(user1, user2)
        every { publicUserRepository.findAllByTier(TierTypeDto.TIER2, any()) } returns
            listOf(user3, user4)

        publicUserService.getLeaderboard(0, 10, TierTypeDto.TIER1).forEach { user ->
            assertThat(user.user.tier).isEqualTo(TierTypeDto.TIER1)
        }
        publicUserService.getLeaderboard(0, 10, TierTypeDto.TIER2).forEach { user ->
            assertThat(user.user.tier).isEqualTo(TierTypeDto.TIER2)
        }

        verify { publicUserRepository.findAllByTier(TierTypeDto.TIER1, any()) }
        verify { publicUserRepository.findAllByTier(TierTypeDto.TIER2, any()) }
        confirmVerified(publicUserRepository)
    }

    @Test
    fun `should promote and demote player tiers`() {
        every { publicUserRepository.findAllByTier(TierTypeDto.TIER1, any()) } returns
            listOf(user2, user1)
        every { publicUserRepository.findAllByTier(TierTypeDto.TIER2, any()) } returns
            listOf(user3, user4)
        every { publicUserRepository.save(any()) } returns publicUserEntity
        publicUserService.promoteTiers()
        verify { publicUserRepository.save(any()) }
        verify { publicUserRepository.findAllByTier(any(), any()) }
        confirmVerified(publicUserRepository)
    }
}
