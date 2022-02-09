package delta.codecharacter.server.game

import com.fasterxml.jackson.databind.ObjectMapper
import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.game.queue.entities.GameRequestEntity
import delta.codecharacter.server.params.GameConfiguration
import delta.codecharacter.server.params.GameParameters
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.HttpStatus
import java.util.Optional
import java.util.UUID

internal class GameServiceTest {
    private lateinit var gameRepository: GameRepository
    private lateinit var gameService: GameService
    private lateinit var rabbitTemplate: RabbitTemplate
    private lateinit var mapper: ObjectMapper
    private lateinit var gameParameters: GameParameters

    @BeforeEach
    fun setUp() {
        gameRepository = mockk()
        rabbitTemplate = mockk()
        mapper = ObjectMapper()
        val gameConfiguration = GameConfiguration()
        gameParameters = gameConfiguration.gameParameters()

        gameService = GameService(gameRepository, rabbitTemplate, gameParameters)
    }

    @Test
    fun `should return game by id`() {
        val game = mockk<GameEntity>()
        val gameId = UUID.randomUUID()

        every { gameRepository.findById(any()) } returns Optional.of(game)

        val result = gameService.getGame(gameId)
        assertEquals(game, result)

        verify { gameRepository.findById(gameId) }
        confirmVerified(gameRepository)
    }

    @Test
    @Throws(CustomException::class)
    fun `should throw exception when game not found`() {
        val gameId = UUID.randomUUID()

        every { gameRepository.findById(any()) } returns Optional.empty()

        val exception = assertThrows(CustomException::class.java) { gameService.getGame(gameId) }

        assertEquals(HttpStatus.NOT_FOUND, exception.status)

        verify { gameRepository.findById(gameId) }
        confirmVerified(gameRepository)
    }

    @Test
    fun `should create game`() {
        val matchId = UUID.randomUUID()
        val game = mockk<GameEntity>()

        every { gameRepository.save(any()) } returns game

        val result = gameService.createGame(matchId)
        assertEquals(game, result)

        verify { gameRepository.save(any()) }
        confirmVerified(gameRepository)
    }

    @Test
    fun `should send game request`() {
        val game = mockk<GameEntity>()
        val gameId = UUID.randomUUID()

        val expectedGameRequest =
            GameRequestEntity(
                gameId = gameId,
                sourceCode = "code",
                language = LanguageEnum.CPP,
                parameters = gameParameters,
                map = "[[0]]"
            )

        every { game.id } returns gameId
        every {
            rabbitTemplate.convertAndSend(
                "gameRequestQueue", mapper.writeValueAsString(expectedGameRequest)
            )
        } returns Unit

        gameService.sendGameRequest(game, "code", LanguageEnum.CPP, "[[0]]")

        verify {
            rabbitTemplate.convertAndSend(
                "gameRequestQueue", mapper.writeValueAsString(expectedGameRequest)
            )
        }
        confirmVerified(rabbitTemplate)
    }
}
