package delta.codecharacter.server.game

import com.fasterxml.jackson.databind.ObjectMapper
import delta.codecharacter.server.TestAttributes
import delta.codecharacter.server.WithMockCustomUser
import delta.codecharacter.server.game.game_log.GameLogEntity
import delta.codecharacter.server.user.UserEntity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.dropCollection
import org.springframework.http.MediaType
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.util.UUID

@AutoConfigureMockMvc
@SpringBootTest
internal class GameControllerIntegrationTest(@Autowired val mockMvc: MockMvc) {
    @Autowired private lateinit var mongoTemplate: MongoTemplate

    @Autowired private lateinit var jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        mapper = jackson2ObjectMapperBuilder.build()
        mongoTemplate.save<UserEntity>(TestAttributes.user)
    }

    @Test
    @WithMockCustomUser
    fun `should get game log`() {
        val gameEntity =
            GameEntity(
                id = UUID.randomUUID(),
                destruction = 100.0,
                coinsUsed = 100,
                status = GameStatusEnum.EXECUTED,
                verdict = GameVerdictEnum.TIE,
                matchId = UUID.randomUUID(),
            )
        mongoTemplate.save<GameEntity>(gameEntity)

        val gameLogEntity = GameLogEntity(gameId = gameEntity.id, log = "game log")
        mongoTemplate.save<GameLogEntity>(gameLogEntity)

        mockMvc
            .get("/games/${gameEntity.id}/logs") { contentType = MediaType.APPLICATION_JSON }
            .andExpect {
                status { is2xxSuccessful() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { string(gameLogEntity.log) }
            }
    }

    @Test
    @WithMockCustomUser
    fun `should return empty string when game not found`() {
        val randomUUID = UUID.randomUUID()

        mockMvc.get("/games/$randomUUID/logs") { contentType = MediaType.APPLICATION_JSON }.andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { string("") }
        }
    }

    @AfterEach
    fun tearDown() {
        mongoTemplate.dropCollection<UserEntity>()
        mongoTemplate.dropCollection<GameEntity>()
        mongoTemplate.dropCollection<GameLogEntity>()
    }
}
