package delta.codecharacter.server.game.game_log

import delta.codecharacter.server.game.GameService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GameLogService(
    @Autowired private val gameLogRepository: GameLogRepository,
    @Autowired private val gameService: GameService
) {

    fun getGameLog(gameId: UUID): String {
        val gameEntity = gameService.getGame(gameId)
        val gameLog = gameLogRepository.findById(gameEntity)
        return if (gameLog.isPresent) {
            gameLog.get().log
        } else {
            ""
        }
    }
}
