package delta.codecharacter.server.daily_challenge

import delta.codecharacter.dtos.DailyChallengeGetRequestDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Service
class DailyChallengeService(
    @Autowired private val dailyChallengeRepository: DailyChallengeRepository
) {

    @Value("\${environment.event-start-date}") val tempDate: String = ""

    fun findNumberOfDays(): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss", Locale.ENGLISH)

        val givenDateTime =
            LocalDateTime.of(LocalDate.parse(tempDate, formatter), LocalTime.of(0, 0))
                .atZone(ZoneId.of("Asia/Kolkata"))
        val zdtNow = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"))
        val period: Period = Period.between(givenDateTime.toLocalDate(), zdtNow.toLocalDate())
        return period.days
    }

    fun getDailyChallengeByDate(): DailyChallengeGetRequestDto {

        val dc = dailyChallengeRepository.findByDay(findNumberOfDays())
        return DailyChallengeGetRequestDto(
            challName = dc.challName,
            chall = dc.chall,
            challType = dc.challType,
            description = dc.description,
            completionStatus = dc.completionStatus
        )
    }
}
