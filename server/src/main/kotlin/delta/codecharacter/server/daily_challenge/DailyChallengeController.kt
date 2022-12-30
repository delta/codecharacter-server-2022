package delta.codecharacter.server.daily_challenge

import delta.codecharacter.core.DailyChallengesApi
import delta.codecharacter.dtos.DailyChallengeGetRequestDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class DailyChallengeController(@Autowired private val dailyChallengeService: DailyChallengeService) : DailyChallengesApi {
    override fun getDailyChallenge(): ResponseEntity<DailyChallengeGetRequestDto> {
        return ResponseEntity.ok(dailyChallengeService.getDailyChallengeByDate())
    }
}
