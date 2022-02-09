package delta.codecharacter.server.game

import delta.codecharacter.core.GameApi
import delta.codecharacter.server.game.game_log.GameLogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class GameController(@Autowired private val gameLogService: GameLogService) : GameApi {

    @Secured(value = ["ROLE_USER"])
    override fun getGameLogsByGameId(gameId: UUID): ResponseEntity<String> {
        return ResponseEntity.ok(gameLogService.getGameLog(gameId))
    }
}
