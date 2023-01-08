package delta.codecharacter.server.daily_challenge

import delta.codecharacter.dtos.DailyChallengeGetRequestDto
import delta.codecharacter.server.exception.CustomException
import org.apache.http.HttpStatus
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
            completionStatus = true
        )
    }
}
