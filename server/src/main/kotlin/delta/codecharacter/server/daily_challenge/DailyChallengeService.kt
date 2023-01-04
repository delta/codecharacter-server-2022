package delta.codecharacter.server.daily_challenge

import delta.codecharacter.dtos.DailyChallengeGetRequestDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Calendar
import java.util.concurrent.TimeUnit

@Service
class DailyChallengeService(
    @Autowired private val dailyChallengeRepository: DailyChallengeRepository
) {
    @Value("\${EVENT_START_DATE}") lateinit var tempDate: String
    fun getDailyChallengeByDate(): DailyChallengeGetRequestDto {

        val date: List<String> = tempDate.split(" ")

        val startDate = Calendar.getInstance()
        startDate.set(date[0].toInt(), date[1].toInt(), date[2].toInt())

        val millionSeconds = Calendar.getInstance().timeInMillis - startDate.timeInMillis
        val numberOfDays = TimeUnit.MILLISECONDS.toDays(millionSeconds).toInt()

        val dc = dailyChallengeRepository.findByDay(numberOfDays)
        return DailyChallengeGetRequestDto(
            challName = dc.challName, chall = dc.chall, challType = dc.challType
        )
    }
}
