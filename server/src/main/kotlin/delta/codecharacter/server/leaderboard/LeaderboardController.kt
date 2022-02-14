package delta.codecharacter.server.leaderboard

import delta.codecharacter.core.LeaderboardApi
import delta.codecharacter.dtos.LeaderboardEntryDto
import delta.codecharacter.server.user.public_user.PublicUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class LeaderboardController(@Autowired private val publicUserService: PublicUserService) :
    LeaderboardApi {
    override fun getLeaderboard(page: Int?, size: Int?): ResponseEntity<List<LeaderboardEntryDto>> {
        return ResponseEntity.ok(publicUserService.getLeaderboard(page, size))
    }
}
