package delta.codecharacter.server.match

import com.fasterxml.jackson.databind.ObjectMapper
import delta.codecharacter.dtos.CodeTypeDto
import delta.codecharacter.dtos.CreateMatchRequestDto
import delta.codecharacter.dtos.GameMapTypeDto
import delta.codecharacter.dtos.MatchModeDto
import delta.codecharacter.server.TestAttributes
import delta.codecharacter.server.WithMockCustomUser
import delta.codecharacter.server.code.Code
import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.code.code_revision.CodeRevisionEntity
import delta.codecharacter.server.code.locked_code.LockedCodeEntity
import delta.codecharacter.server.game.GameEntity
import delta.codecharacter.server.game.GameStatusEnum
import delta.codecharacter.server.game.game_log.GameLogEntity
import delta.codecharacter.server.game.queue.entities.GameRequestEntity
import delta.codecharacter.server.game.queue.entities.GameResultEntity
import delta.codecharacter.server.game.queue.entities.GameStatusUpdateEntity
import delta.codecharacter.server.game_map.GameMap
import delta.codecharacter.server.game_map.locked_map.LockedMapEntity
import delta.codecharacter.server.game_map.map_revision.MapRevisionEntity
import delta.codecharacter.server.user.UserEntity
import delta.codecharacter.server.user.public_user.PublicUserEntity
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
        mongoTemplate.save<PublicUserEntity>(TestAttributes.publicUser)
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
                    userId = TestAttributes.user.id,
                    code = "code",
                    codeType = CodeTypeDto.NORMAL,
                    message = "message",
                    language = LanguageEnum.PYTHON,
                    createdAt = Instant.now(),
                    parentRevision = null
                )
            )

        val mapRevision =
            mongoTemplate.save(
                MapRevisionEntity(
                    id = UUID.randomUUID(),
                    userId = TestAttributes.user.id,
                    map = "map",
                    mapType = GameMapTypeDto.NORMAL,
                    mapImage = "",
                    message = "message",
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
        assertThat(matches.size).isEqualTo(1)

        val match = matches.first()
        assertThat(match.mode).isEqualTo(MatchModeEnum.SELF)
        assertThat(match.games.size).isEqualTo(1)

        val games = mongoTemplate.findAll<GameEntity>()
        assertThat(games.size).isEqualTo(1)

        val game = games.first()
        assertThat(game.status).isEqualTo(GameStatusEnum.IDLE)
        assertThat(match.games.first()).isEqualTo(game)

        val gameRequestEntityString =
            this.rabbitTemplate.receiveAndConvert("gameRequestQueue") as String
        val gameRequestEntity = mapper.readValue(gameRequestEntityString, GameRequestEntity::class.java)
        assertThat(gameRequestEntity.gameId).isEqualTo(game.id)
        assertThat(gameRequestEntity.map).isEqualTo(mapRevision.map)
        assertThat(gameRequestEntity.sourceCode).isEqualTo(codeRevision.code)
        assertThat(gameRequestEntity.language).isEqualTo(codeRevision.language)
    }

    @Test
    @WithMockCustomUser
    fun `should create match with two games for manual mode`() {
        val lockedUserCode = HashMap<CodeTypeDto, Code>()
        lockedUserCode[CodeTypeDto.NORMAL] = Code(code = "user-code", language = LanguageEnum.PYTHON)

        val lockedUserMap = HashMap<GameMapTypeDto, GameMap>()
        lockedUserMap[GameMapTypeDto.NORMAL] = GameMap(map = "user-map", mapImage = "base64")
        val opponentUser =
            mongoTemplate.save(
                TestAttributes.user.copy(id = UUID.randomUUID(), email = "opponent@test.com")
            )
        val opponentPublicUser =
            mongoTemplate.save(
                TestAttributes.publicUser.copy(
                    userId = opponentUser.id, username = opponentUser.username
                )
            )

        val userLockedCode =
            mongoTemplate.save(
                LockedCodeEntity(userId = TestAttributes.user.id, lockedCode = lockedUserCode)
            )
        val userLockedMap =
            mongoTemplate.save(
                LockedMapEntity(userId = TestAttributes.user.id, lockedMap = lockedUserMap)
            )
        val lockedOpponentCode = HashMap<CodeTypeDto, Code>()
        lockedUserCode[CodeTypeDto.NORMAL] =
            Code(code = "opponent-code", language = LanguageEnum.PYTHON)

        val lockedOpponentMap = HashMap<GameMapTypeDto, GameMap>()
        lockedUserMap[GameMapTypeDto.NORMAL] = GameMap(map = "opponent-map", mapImage = "base64")
        val opponentLockedCode =
            mongoTemplate.save(
                LockedCodeEntity(userId = opponentUser.id, lockedCode = lockedOpponentCode)
            )
        val opponentLockedMap =
            mongoTemplate.save(LockedMapEntity(userId = opponentUser.id, lockedMap = lockedOpponentMap))

        val createMatchRequestDto =
            CreateMatchRequestDto(
                mode = MatchModeDto.MANUAL,
                opponentUsername = opponentPublicUser.username,
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
            this.rabbitTemplate.receiveAndConvert("gameRequestQueue") as String?
        val gameRequestEntity1 =
            mapper.readValue(gameRequestEntityString1, GameRequestEntity::class.java)
        if (opponentLockedMap.lockedMap[GameMapTypeDto.NORMAL] != null &&
            userLockedCode.lockedCode[CodeTypeDto.NORMAL] != null
        ) {
            assertThat(gameRequestEntity1.gameId).isEqualTo(game1.id)
            assertThat(gameRequestEntity1.map)
                .isEqualTo(opponentLockedMap.lockedMap[GameMapTypeDto.NORMAL]?.map.toString())
            assertThat(gameRequestEntity1.sourceCode)
                .isEqualTo(userLockedCode.lockedCode[CodeTypeDto.NORMAL]?.code)
            assertThat(gameRequestEntity1.language)
                .isEqualTo(userLockedCode.lockedCode[CodeTypeDto.NORMAL]?.language)
        }
        val gameRequestEntityString2 =
            this.rabbitTemplate.receiveAndConvert("gameRequestQueue") as String?
        val gameRequestEntity2 =
            mapper.readValue(gameRequestEntityString2, GameRequestEntity::class.java)
        if (userLockedMap.lockedMap[GameMapTypeDto.NORMAL] != null &&
            opponentLockedCode.lockedCode[CodeTypeDto.NORMAL] != null
        ) {
            assertThat(gameRequestEntity2.gameId).isEqualTo(game2.id)
            assertThat(gameRequestEntity2.map)
                .isEqualTo(userLockedMap.lockedMap[GameMapTypeDto.NORMAL]?.map)
            assertThat(gameRequestEntity2.sourceCode)
                .isEqualTo(opponentLockedCode.lockedCode[CodeTypeDto.NORMAL]?.code)
            assertThat(gameRequestEntity2.language)
                .isEqualTo(opponentLockedCode.lockedCode[CodeTypeDto.NORMAL]?.language)
        }
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
                    destruction = 0.0,
                    matchId = UUID.randomUUID()
                )
            )
        mongoTemplate.save(
            MatchEntity(
                id = game.matchId,
                games = listOf(game),
                mode = MatchModeEnum.SELF,
                verdict = MatchVerdictEnum.TIE,
                createdAt = Instant.now(),
                totalPoints = 0,
                player1 = TestAttributes.publicUser,
                player2 = TestAttributes.publicUser,
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

    @Test
    @Timeout(value = 5000, unit = TimeUnit.MILLISECONDS)
    @WithMockCustomUser
    fun `should update the game status from game result queue with game result`() {
        val game =
            mongoTemplate.save(
                GameEntity(
                    id = UUID.randomUUID(),
                    status = GameStatusEnum.IDLE,
                    coinsUsed = 0,
                    destruction = 0.0,
                    matchId = UUID.randomUUID()
                )
            )
        mongoTemplate.save(
            MatchEntity(
                id = game.matchId,
                games = listOf(game),
                mode = MatchModeEnum.SELF,
                verdict = MatchVerdictEnum.TIE,
                createdAt = Instant.now(),
                totalPoints = 0,
                player1 = TestAttributes.publicUser,
                player2 = TestAttributes.publicUser,
            )
        )

        val gameResult =
            GameResultEntity(
                destructionPercentage = 60.0, coinsUsed = 100, hasErrors = false, log = "log"
            )
        val gameStatusUpdate =
            GameStatusUpdateEntity(
                gameId = game.id, gameStatus = GameStatusEnum.EXECUTED, gameResult = gameResult
            )
        rabbitTemplate.convertAndSend(
            "gameStatusUpdateQueue", mapper.writeValueAsString(gameStatusUpdate)
        )

        while (mongoTemplate.findById<GameEntity>(game.id)?.status == GameStatusEnum.IDLE) {
            println("Waiting for game status update to be processed...")
            Thread.sleep(100)
        }
        val updatedGame = mongoTemplate.findById<GameEntity>(game.id)
        assertThat(updatedGame?.status).isEqualTo(GameStatusEnum.EXECUTED)
        assertThat(updatedGame?.coinsUsed).isEqualTo(gameResult.coinsUsed)
        assertThat(updatedGame?.destruction).isEqualTo(gameResult.destructionPercentage)

        val gameLog = mongoTemplate.findById<GameLogEntity>(game.id)
        assertThat(gameLog?.log).isEqualTo(gameResult.log)
    }

    @AfterEach
    fun tearDown() {
        mongoTemplate.dropCollection<UserEntity>()
        mongoTemplate.dropCollection<PublicUserEntity>()
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
