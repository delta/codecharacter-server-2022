package delta.codecharacter.server.schedulers

import delta.codecharacter.server.match.MatchService
import delta.codecharacter.server.user.public_user.PublicUserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SchedulingService(
    @Autowired private val publicUserService: PublicUserService,
    @Autowired private val matchService: MatchService
) {
    private val logger: Logger = LoggerFactory.getLogger(SchedulingService::class.java)

    @Scheduled(cron = "\${environment.registration-time}", zone = "GMT+5:30")
    fun updateTempLeaderboard() {
        logger.info("Practice phase ended!!")
        publicUserService.resetRatingsAfterPracticePhase()
        publicUserService.updateLeaderboardAfterPracticePhase()
    }

    @Scheduled(cron = "\${environment.promote-demote-time}", zone = "GMT+5:30")
    fun createAutoMatch() {
        matchService.createAutoMatch()
    }
}
