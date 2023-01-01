package delta.codecharacter.server.leaderboard

import delta.codecharacter.core.DailyChallengesApi
import delta.codecharacter.dtos.DailyChallengeLeaderBoardResponseDto
import delta.codecharacter.server.user.public_user.PublicUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class DailyChallengeLeaderboardController(@Autowired private val publicUserService: PublicUserService) :
    DailyChallengesApi {
    override fun getDailyChallengeLeaderBoard(): ResponseEntity<List<DailyChallengeLeaderBoardResponseDto>> {
        return ResponseEntity.ok(publicUserService.getDailyChallengeLeaderboard())
    }
}
