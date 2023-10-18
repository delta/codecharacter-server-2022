package delta.codecharacter.server.daily_challenge

import delta.codecharacter.server.TestAttributes
import delta.codecharacter.server.daily_challenge.match.DailyChallengeMatchVerdictEnum
import delta.codecharacter.server.game.GameEntity
import delta.codecharacter.server.game.GameStatusEnum
import delta.codecharacter.server.logic.daily_challenge_score.DailyChallengeScoreAlgorithm
import delta.codecharacter.server.user.public_user.PublicUserService
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils
import java.util.Optional
import java.util.UUID
import kotlin.collections.HashMap

internal class DailyChallengeServiceTest {
    private lateinit var dailyChallengeRepository: DailyChallengeRepository
    private lateinit var publicUserService: PublicUserService
    private lateinit var dailyChallengeScoreAlgorithm: DailyChallengeScoreAlgorithm
    private lateinit var dailyChallengeService: DailyChallengeService

    @BeforeEach
    fun setUp() {
        dailyChallengeRepository = mockk(relaxed = true)
        publicUserService = mockk(relaxed = true)
        dailyChallengeScoreAlgorithm = mockk(relaxed = true)
        dailyChallengeService =
            DailyChallengeService(
                dailyChallengeRepository, publicUserService, dailyChallengeScoreAlgorithm
            )

        ReflectionTestUtils.setField(dailyChallengeService, "startDate", "2023-02-15T23:00:00Z")
        every { publicUserService.getPublicUser(any()) } returns
            TestAttributes.publicUser.copy(dailyChallengeHistory = HashMap())
        every { dailyChallengeRepository.findByDay(any()) } returns
            Optional.ofNullable(TestAttributes.dailyChallengeCode)
    }

    @Test
    fun `should return daily challenge for User`() {
        assertThat(
            dailyChallengeService.getDailyChallengeByDateForUser(UUID.randomUUID())
                .completionStatus
        )
            .isEqualTo(false)
    }

    @Test
    fun `should return failure if game had errors`() {
        val gameEntity =
            GameEntity(
                id = UUID.randomUUID(),
                coinsUsed = 2000,
                destruction = 35.0,
                status = GameStatusEnum.EXECUTE_ERROR,
                matchId = UUID.randomUUID()
            )
        assertThat(dailyChallengeService.completeDailyChallenge(gameEntity, UUID.randomUUID()))
            .isEqualTo(DailyChallengeMatchVerdictEnum.FAILURE)
    }

    @Test
    fun `should return success if destruction met the required criteria`() {
        val gameEntity =
            GameEntity(
                id = UUID.randomUUID(),
                coinsUsed = 2000,
                destruction = 35.0,
                status = GameStatusEnum.EXECUTED,
                matchId = UUID.randomUUID()
            )
        every { dailyChallengeRepository.save(any()) } returns mockk()
        assertThat(dailyChallengeService.completeDailyChallenge(gameEntity, UUID.randomUUID()))
            .isEqualTo(DailyChallengeMatchVerdictEnum.SUCCESS)
    }
}
