package delta.codecharacter.server.daily_challenge

import delta.codecharacter.dtos.DailyChallengeGetRequestDto
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.game.GameEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

@Service
class DailyChallengeService(
    @Autowired private val dailyChallengeRepository: DailyChallengeRepository
) {

    @Value("\${environment.event-start-date}") val tempDate: String = ""

    fun findNumberOfDays(): Int {
        val givenDateTime = Instant.parse(tempDate)
        val nowDateTime = Instant.now()
        val period: Duration = Duration.between(givenDateTime, nowDateTime)
        return period.toDays().toInt()
    }

    fun getDailyChallengeByDate(): DailyChallengeGetRequestDto {

        val dc =
            dailyChallengeRepository.findByDay(findNumberOfDays()).orElseThrow {
                throw CustomException(org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid Request")
            }
        return DailyChallengeGetRequestDto(
            challName = dc.challName,
            chall = dc.chall,
            challType = dc.challType,
            description = dc.description,
            completionStatus = false
        )
    }

    fun completeDailyChallenge(gameEntity: GameEntity) {
        val (_, coinsUsed, destruction, status, matchId) = gameEntity
    /*
     * if destruction >= 60 --> completed --> usr can not play
     * else not-completed user can play
     *
     * score = base-score + (value depending on coinsUsed and destruction %)
     * base-score will get reduced as player starts solving challenge
     * base-score = base-score - rv (rv-> reducing value) (like CTF style)
     *
     * Corresponding LeaderBoard updates
     *
     * Scheduling for isDailyChallengeComplete -> false by 24hrs
     */
    }
}
