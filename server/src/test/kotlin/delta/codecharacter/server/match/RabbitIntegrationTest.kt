package delta.codecharacter.server.match

import com.fasterxml.jackson.databind.ObjectMapper
import delta.codecharacter.dtos.CreateMatchRequestDto
import delta.codecharacter.dtos.MatchModeDto
import delta.codecharacter.server.TestAttributes
import delta.codecharacter.server.WithMockCustomUser
import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.code.code_revision.CodeRevisionEntity
import delta.codecharacter.server.code.locked_code.LockedCodeEntity
import delta.codecharacter.server.game.GameEntity
import delta.codecharacter.server.game.GameStatusEnum
import delta.codecharacter.server.game.GameVerdictEnum
import delta.codecharacter.server.game.game_log.GameLogEntity
import delta.codecharacter.server.game.queue.entities.GameRequestEntity
import delta.codecharacter.server.game.queue.entities.GameStatusUpdateEntity
import delta.codecharacter.server.game_map.locked_map.LockedMapEntity
import delta.codecharacter.server.game_map.map_revision.MapRevisionEntity
import delta.codecharacter.server.user.UserEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.junit.RabbitAvailable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.dropCollection
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.findById
import org.springframework.http.MediaType
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.Instant
import java.util.UUID
import java.util.concurrent.TimeUnit

@AutoConfigureMockMvc
@SpringBootTest
@RabbitAvailable
internal class RabbitIntegrationTest(@Autowired val mockMvc: MockMvc) {
    @Autowired private lateinit var rabbitTemplate: RabbitTemplate

    @Autowired private lateinit var rabbitAdmin: RabbitAdmin

    @Autowired private lateinit var mongoTemplate: MongoTemplate

    @Autowired private lateinit var jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        mapper = jackson2ObjectMapperBuilder.build()
        mongoTemplate.save<UserEntity>(TestAttributes.user)
        try {
            rabbitAdmin.purgeQueue("gameRequestQueue", true)
            rabbitAdmin.purgeQueue("gameStatusUpdateQueue", true)
        } catch (e: Exception) {
            println("RabbitMQ queues are not available")
        }
    }

    @Test
    @WithMockCustomUser
    fun `should create a match with one game for self mode`() {
        val codeRevision =
            mongoTemplate.save(
                CodeRevisionEntity(
                    id = UUID.randomUUID(),
                    user = TestAttributes.user,
                    code = "code",
                    language = LanguageEnum.PYTHON,
                    createdAt = Instant.now(),
                    parentRevision = null
                )
            )

        val mapRevision =
            mongoTemplate.save(
                MapRevisionEntity(
                    id = UUID.randomUUID(),
                    user = TestAttributes.user,
                    map = "map",
                    createdAt = Instant.now(),
                    parentRevision = null
                )
            )

        val createMatchRequestDto =
            CreateMatchRequestDto(
                mode = MatchModeDto.SELF,
                codeRevisionId = codeRevision.id,
                mapRevisionId = mapRevision.id
            )

        mockMvc
            .post("/user/matches") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(createMatchRequestDto)
            }
            .andExpect { status { is2xxSuccessful() } }

        val matches = mongoTemplate.findAll<MatchEntity>()
        assert(matches.size == 1)

        val match = matches.first()
        assert(match.mode == MatchModeEnum.SELF)
        assert(match.games.size == 1)

        val games = mongoTemplate.findAll<GameEntity>()
        assert(games.size == 1)

        val game = games.first()
        assert(game.status == GameStatusEnum.IDLE)
        assert(match.games.first() == game)

        val gameRequestEntityString =
            this.rabbitTemplate.receiveAndConvert("gameRequestQueue") as String
        val gameRequestEntity = mapper.readValue(gameRequestEntityString, GameRequestEntity::class.java)
        assert(gameRequestEntity.gameId == game.id)
        assert(gameRequestEntity.map == mapRevision.map)
        assert(gameRequestEntity.sourceCode == codeRevision.code)
        assert(gameRequestEntity.language == codeRevision.language)
    }

    @Test
    @WithMockCustomUser
    fun `should create match with two games for manual mode`() {
        val opponentUser =
            mongoTemplate.save(
                TestAttributes.user.copy(id = UUID.randomUUID(), email = "opponent@test.com")
            )

        val userLockedCode =
            mongoTemplate.save(
                LockedCodeEntity(
                    user = TestAttributes.user,
                    code = "user-code",
                    language = LanguageEnum.PYTHON,
                )
            )
        val userLockedMap =
            mongoTemplate.save(
                LockedMapEntity(
                    user = TestAttributes.user,
                    map = "user-map",
                )
            )

        val opponentLockedCode =
            mongoTemplate.save(
                LockedCodeEntity(
                    user = opponentUser,
                    code = "opponent-code",
                    language = LanguageEnum.PYTHON,
                )
            )
        val opponentLockedMap =
            mongoTemplate.save(
                LockedMapEntity(
                    user = opponentUser,
                    map = "opponent-map",
                )
            )

        val createMatchRequestDto =
            CreateMatchRequestDto(
                mode = MatchModeDto.MANUAL,
                opponentId = opponentUser.id,
            )

        mockMvc
            .post("/user/matches") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(createMatchRequestDto)
            }
            .andExpect { status { is2xxSuccessful() } }

        val matches = mongoTemplate.findAll<MatchEntity>()
        assertThat(matches.size).isEqualTo(1)

        val match = matches.first()
        assertThat(match.mode).isEqualTo(MatchModeEnum.MANUAL)
        assertThat(match.games.size).isEqualTo(2)

        val games = mongoTemplate.findAll<GameEntity>()
        assertThat(games.size).isEqualTo(2)

        val game1 = games.first()
        assertThat(game1.status).isEqualTo(GameStatusEnum.IDLE)
        assertThat(match.games.first()).isEqualTo(game1)

        val game2 = games.last()
        assertThat(game2.status).isEqualTo(GameStatusEnum.IDLE)
        assertThat(match.games.last()).isEqualTo(game2)

        val gameRequestEntityString1 =
            this.rabbitTemplate.receiveAndConvert("gameRequestQueue") as String
        val gameRequestEntity1 =
            mapper.readValue(gameRequestEntityString1, GameRequestEntity::class.java)
        assertThat(gameRequestEntity1.gameId).isEqualTo(game1.id)
        assertThat(gameRequestEntity1.map).isEqualTo(opponentLockedMap.map)
        assertThat(gameRequestEntity1.sourceCode).isEqualTo(userLockedCode.code)
        assertThat(gameRequestEntity1.language).isEqualTo(userLockedCode.language)

        val gameRequestEntityString2 =
            this.rabbitTemplate.receiveAndConvert("gameRequestQueue") as String
        val gameRequestEntity2 =
            mapper.readValue(gameRequestEntityString2, GameRequestEntity::class.java)
        assertThat(gameRequestEntity2.gameId).isEqualTo(game2.id)
        assertThat(gameRequestEntity2.map).isEqualTo(userLockedMap.map)
        assertThat(gameRequestEntity2.sourceCode).isEqualTo(opponentLockedCode.code)
        assertThat(gameRequestEntity2.language).isEqualTo(opponentLockedCode.language)
    }

    @Test
    @Timeout(value = 5000, unit = TimeUnit.MILLISECONDS)
    @WithMockCustomUser
    fun `should update the game status from game result queue`() {
        val game =
            mongoTemplate.save(
                GameEntity(
                    id = UUID.randomUUID(),
                    status = GameStatusEnum.IDLE,
                    coinsUsed = 0,
                    destruction = 0F,
                    verdict = GameVerdictEnum.TIE,
                    matchId = UUID.randomUUID()
                )
            )

        val gameStatusUpdate =
            GameStatusUpdateEntity(
                gameId = game.id, gameStatus = GameStatusEnum.EXECUTING, gameResult = null
            )
        rabbitTemplate.convertAndSend(
            "gameStatusUpdateQueue", mapper.writeValueAsString(gameStatusUpdate)
        )

        // TODO: Find better way to wait for listener to complete
        while (mongoTemplate.findById<GameEntity>(game.id)?.status == GameStatusEnum.IDLE) {
            println("Waiting for game status update to be processed...")
            Thread.sleep(100)
        }
        val updatedGame = mongoTemplate.findById<GameEntity>(game.id)
        assertThat(updatedGame?.status).isEqualTo(GameStatusEnum.EXECUTING)
    }

    @AfterEach
    fun tearDown() {
        mongoTemplate.dropCollection<UserEntity>()
        mongoTemplate.dropCollection<GameEntity>()
        mongoTemplate.dropCollection<GameLogEntity>()
        mongoTemplate.dropCollection<MatchEntity>()
        mongoTemplate.dropCollection<LockedCodeEntity>()
        mongoTemplate.dropCollection<LockedMapEntity>()
        mongoTemplate.dropCollection<CodeRevisionEntity>()
        mongoTemplate.dropCollection<MapRevisionEntity>()

        try {
            rabbitAdmin.purgeQueue("gameRequestQueue", true)
            rabbitAdmin.purgeQueue("gameStatusUpdateQueue", true)
        } catch (e: Exception) {
            println("RabbitMQ queues are not available")
        }
    }
}
