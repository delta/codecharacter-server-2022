package delta.codecharacter.server.game.game_log

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GameLogService(@Autowired private val gameLogRepository: GameLogRepository) {

    fun getGameLog(gameId: UUID): String {
        val gameLog = gameLogRepository.findById(gameId)
        return if (gameLog.isPresent) {
            gameLog.get().log
        } else {
            ""
        }
    }

    fun saveGameLog(gameId: UUID, log: String) {
        val gameLog = GameLogEntity(gameId, log)
        gameLogRepository.save(gameLog)
    }
}
