package delta.codecharacter.server.game

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.game.game_log.GameLogService
import delta.codecharacter.server.game.queue.entities.GameRequestEntity
import delta.codecharacter.server.game.queue.entities.GameStatusUpdateEntity
import delta.codecharacter.server.params.GameParameters
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GameService(
    @Autowired private val gameRepository: GameRepository,
    @Autowired private val gameLogService: GameLogService,
    @Autowired private val rabbitTemplate: RabbitTemplate,
    @Autowired private val parameters: GameParameters,
) {
    private var mapper = ObjectMapper().registerKotlinModule()

    fun getGame(id: UUID): GameEntity {
        return gameRepository.findById(id).orElseThrow {
            throw CustomException(HttpStatus.NOT_FOUND, "Game not found")
        }
    }

    fun createGame(matchId: UUID): GameEntity {
        val game =
            GameEntity(
                id = UUID.randomUUID(),
                coinsUsed = 0,
                destruction = 0.0,
                status = GameStatusEnum.IDLE,
                matchId = matchId,
            )
        return gameRepository.save(game)
    }

    fun sendGameRequest(game: GameEntity, sourceCode: String, language: LanguageEnum, map: String) {
        val gameRequest =
            GameRequestEntity(
                gameId = game.id,
                sourceCode = sourceCode,
                language = language,
                parameters = parameters,
                map = map
            )
        rabbitTemplate.convertAndSend("gameRequestQueue", mapper.writeValueAsString(gameRequest))
    }

    fun updateGameStatus(gameStatusUpdateJson: String): GameEntity {
        val gameStatusUpdateEntity =
            mapper.readValue(gameStatusUpdateJson, GameStatusUpdateEntity::class.java)

        val oldGameEntity =
            gameRepository.findById(gameStatusUpdateEntity.gameId).orElseThrow {
                throw CustomException(HttpStatus.NOT_FOUND, "Game not found")
            }
        if (gameStatusUpdateEntity.gameResult == null) {
            val newGameEntity = oldGameEntity.copy(status = gameStatusUpdateEntity.gameStatus)
            return gameRepository.save(newGameEntity)
        }

        val gameResult = gameStatusUpdateEntity.gameResult

        val (destructionPercentage, coinsUsed) = gameResult
        val gameStatus =
            if (gameResult.hasErrors) {
                GameStatusEnum.EXECUTE_ERROR
            } else {
                GameStatusEnum.EXECUTED
            }

        val newGameEntity =
            oldGameEntity.copy(
                destruction = destructionPercentage, coinsUsed = coinsUsed, status = gameStatus
            )
        val game = gameRepository.save(newGameEntity)
        gameLogService.saveGameLog(game.id, gameResult.log)
        return game
    }
}
