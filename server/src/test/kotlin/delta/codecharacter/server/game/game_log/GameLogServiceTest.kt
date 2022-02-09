package delta.codecharacter.server.game.game_log

import delta.codecharacter.server.game.GameEntity
import delta.codecharacter.server.game.GameService
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Optional
import java.util.UUID

internal class GameLogServiceTest {
    private lateinit var gameLogRepository: GameLogRepository
    private lateinit var gameLogService: GameLogService
    private lateinit var gameService: GameService

    @BeforeEach
    fun setUp() {
        gameLogRepository = mockk()
        gameService = mockk()
        gameLogService = GameLogService(gameLogRepository, gameService)
    }

    @Test
    fun `should return game log`() {
        val gameId = UUID.randomUUID()
        val gameEntity = mockk<GameEntity>()
        val gameLogEntity = mockk<GameLogEntity>()
        val expectedGameLog = "game log"

        every { gameService.getGame(gameId) } returns gameEntity
        every { gameLogRepository.findById(gameEntity) } returns Optional.of(gameLogEntity)
        every { gameLogEntity.log } returns expectedGameLog

        val gameLog = gameLogService.getGameLog(gameId)

        assertEquals(expectedGameLog, gameLog)

        verify { gameService.getGame(gameId) }
        verify { gameLogRepository.findById(gameEntity) }
        verify { gameLogEntity.log }
        confirmVerified(gameLogRepository)
    }
}
