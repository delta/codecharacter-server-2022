package delta.codecharacter.server.daily_challenge

import delta.codecharacter.dtos.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DailyChallengeService(@Autowired private val dailyChallengeRepository: DailyChallengeRepository) {

    fun getDailyChallengeByDate(): DailyChallengeGetRequestDto {
        val dc = dailyChallengeRepository.findAll()[0] // Change this to {Current date - Date on which it was hosted}
        return DailyChallengeGetRequestDto(
                challName = dc.challName,
                chall = dc.chall,
                challType = dc.challType
        )
    }

}
