package delta.codecharacter.server.game.game_log

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

    @BeforeEach
    fun setUp() {
        gameLogRepository = mockk()
        gameLogService = GameLogService(gameLogRepository)
    }

    @Test
    fun `should return game log`() {
        val gameId = UUID.randomUUID()
        val gameLogEntity = mockk<GameLogEntity>()
        val expectedGameLog = "game log"

        every { gameLogRepository.findById(gameId) } returns Optional.of(gameLogEntity)
        every { gameLogEntity.log } returns expectedGameLog

        val gameLog = gameLogService.getGameLog(gameId)

        assertEquals(expectedGameLog, gameLog)

        verify { gameLogRepository.findById(gameId) }
        verify { gameLogEntity.log }
        confirmVerified(gameLogRepository)
    }
}
