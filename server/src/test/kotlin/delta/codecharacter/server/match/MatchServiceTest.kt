package delta.codecharacter.server.match

import delta.codecharacter.dtos.CodeRevisionDto
import delta.codecharacter.dtos.CreateMatchRequestDto
import delta.codecharacter.dtos.GameMapRevisionDto
import delta.codecharacter.dtos.LanguageDto
import delta.codecharacter.dtos.MatchModeDto
import delta.codecharacter.server.TestAttributes
import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.code.code_revision.CodeRevisionService
import delta.codecharacter.server.code.latest_code.LatestCodeService
import delta.codecharacter.server.code.locked_code.LockedCodeService
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.game.GameEntity
import delta.codecharacter.server.game.GameService
import delta.codecharacter.server.game_map.latest_map.LatestMapService
import delta.codecharacter.server.game_map.locked_map.LockedMapService
import delta.codecharacter.server.game_map.map_revision.MapRevisionService
import delta.codecharacter.server.logic.verdict.VerdictAlgorithm
import delta.codecharacter.server.notifications.NotificationService
import delta.codecharacter.server.user.public_user.PublicUserService
import delta.codecharacter.server.user.rating_history.RatingHistoryService
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.messaging.simp.SimpMessagingTemplate
import java.time.Instant
import java.util.UUID

internal class MatchServiceTest {

    private lateinit var matchRepository: MatchRepository
    private lateinit var gameService: GameService
    private lateinit var latestCodeService: LatestCodeService
    private lateinit var codeRevisionService: CodeRevisionService
    private lateinit var lockedCodeService: LockedCodeService
    private lateinit var latestMapService: LatestMapService
    private lateinit var mapRevisionService: MapRevisionService
    private lateinit var lockedMapService: LockedMapService
    private lateinit var publicUserService: PublicUserService
    private lateinit var verdictAlgorithm: VerdictAlgorithm
    private lateinit var ratingHistoryService: RatingHistoryService
    private lateinit var notificationService: NotificationService
    private lateinit var jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder
    private lateinit var simpMessagingTemplate: SimpMessagingTemplate

    private lateinit var matchService: MatchService

    @BeforeEach
    fun setUp() {
        matchRepository = mockk(relaxed = true)
        gameService = mockk(relaxed = true)
        latestCodeService = mockk(relaxed = true)
        codeRevisionService = mockk(relaxed = true)
        lockedCodeService = mockk(relaxed = true)
        latestMapService = mockk(relaxed = true)
        mapRevisionService = mockk(relaxed = true)
        lockedMapService = mockk(relaxed = true)
        publicUserService = mockk(relaxed = true)
        verdictAlgorithm = mockk(relaxed = true)
        ratingHistoryService = mockk(relaxed = true)
        notificationService = mockk(relaxed = true)
        jackson2ObjectMapperBuilder = Jackson2ObjectMapperBuilder()
        simpMessagingTemplate = mockk(relaxed = true)

        matchService =
            MatchService(
                matchRepository,
                gameService,
                latestCodeService,
                codeRevisionService,
                lockedCodeService,
                latestMapService,
                mapRevisionService,
                lockedMapService,
                publicUserService,
                verdictAlgorithm,
                ratingHistoryService,
                notificationService,
                jackson2ObjectMapperBuilder,
                simpMessagingTemplate
            )
    }

    @Test
    @Throws(CustomException::class)
    fun `should throw bad request if code revision doesn't belong to the user`() {
        val userId = UUID.randomUUID()
        val codeRevisionId = UUID.randomUUID()
        val codeRevision =
            CodeRevisionDto(codeRevisionId, "code", "message", LanguageDto.CPP, Instant.now())
        val mapRevisionId = UUID.randomUUID()
        val mapRevision = GameMapRevisionDto(mapRevisionId, "map", Instant.now(), "message")

        val createMatchRequestDto =
            CreateMatchRequestDto(
                mode = MatchModeDto.SELF,
                codeRevisionId = UUID.randomUUID(),
                mapRevisionId = mapRevisionId
            )

        every { codeRevisionService.getCodeRevisions(userId) } returns listOf(codeRevision)
        every { mapRevisionService.getMapRevisions(userId) } returns listOf(mapRevision)

        val exception =
            assertThrows<CustomException> { matchService.createMatch(userId, createMatchRequestDto) }

        assertThat(exception.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exception.message).isEqualTo("Invalid revision ID")
    }

    @Test
    @Throws(CustomException::class)
    fun `should throw bad request if map revision doesn't belong to the user`() {
        val userId = UUID.randomUUID()
        val codeRevisionId = UUID.randomUUID()
        val codeRevision =
            CodeRevisionDto(codeRevisionId, "code", "message", LanguageDto.CPP, Instant.now())
        val mapRevisionId = UUID.randomUUID()
        val mapRevision = GameMapRevisionDto(mapRevisionId, "map", Instant.now(), "message")

        val createMatchRequestDto =
            CreateMatchRequestDto(
                mode = MatchModeDto.SELF,
                codeRevisionId = codeRevisionId,
                mapRevisionId = UUID.randomUUID()
            )

        every { codeRevisionService.getCodeRevisions(userId) } returns listOf(codeRevision)
        every { mapRevisionService.getMapRevisions(userId) } returns listOf(mapRevision)

        val exception =
            assertThrows<CustomException> { matchService.createMatch(userId, createMatchRequestDto) }

        assertThat(exception.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exception.message).isEqualTo("Invalid revision ID")
    }

    @Test
    fun `should create self match`() {
        val userId = UUID.randomUUID()
        val codeRevisionId = UUID.randomUUID()
        val codeRevision =
            CodeRevisionDto(codeRevisionId, "code", "message", LanguageDto.CPP, Instant.now())
        val mapRevisionId = UUID.randomUUID()
        val mapRevision = GameMapRevisionDto(mapRevisionId, "map", Instant.now(), "message")
        val game = mockk<GameEntity>()

        val createMatchRequestDto =
            CreateMatchRequestDto(
                mode = MatchModeDto.SELF,
                codeRevisionId = codeRevisionId,
                mapRevisionId = mapRevisionId
            )

        every { codeRevisionService.getCodeRevisions(userId) } returns listOf(codeRevision)
        every { mapRevisionService.getMapRevisions(userId) } returns listOf(mapRevision)
        every { gameService.createGame(any()) } returns game
        every { matchRepository.save(any()) } returns mockk()
        every { gameService.sendGameRequest(game, any(), any(), any()) } returns Unit

        matchService.createMatch(userId, createMatchRequestDto)

        verify {
            codeRevisionService.getCodeRevisions(userId)
            mapRevisionService.getMapRevisions(userId)
            gameService.createGame(any())
            matchRepository.save(any())
            gameService.sendGameRequest(game, any(), any(), any())
        }
        confirmVerified(
            codeRevisionService, mapRevisionService, gameService, matchRepository, gameService
        )
    }

    @Test
    @Throws(CustomException::class)
    fun `should throw bad request if opponent id is empty in manual match`() {
        val createMatchRequestDto =
            CreateMatchRequestDto(
                mode = MatchModeDto.MANUAL,
                codeRevisionId = UUID.randomUUID(),
                mapRevisionId = UUID.randomUUID(),
                opponentUsername = null
            )

        val exception =
            assertThrows<CustomException> { matchService.createMatch(mockk(), createMatchRequestDto) }

        assertThat(exception.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exception.message).isEqualTo("Opponent ID is required")
    }

    @Test
    @Throws(CustomException::class)
    fun `should throw bad request if opponent id is empty in auto match`() {
        val createMatchRequestDto =
            CreateMatchRequestDto(
                mode = MatchModeDto.AUTO,
                codeRevisionId = UUID.randomUUID(),
                mapRevisionId = UUID.randomUUID(),
                opponentUsername = null
            )

        val exception =
            assertThrows<CustomException> { matchService.createMatch(mockk(), createMatchRequestDto) }

        assertThat(exception.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exception.message).isEqualTo("Opponent ID is required")
    }

    @Test
    fun `should create manual match`() {
        val userId = UUID.randomUUID()
        val opponentId = UUID.randomUUID()
        val opponentPublicUser =
            TestAttributes.publicUser.copy(userId = opponentId, username = "opponent")

        val userCode = Pair(LanguageEnum.CPP, "user-code")
        val opponentCode = Pair(LanguageEnum.PYTHON, "opponent-code")
        val userMap = "user-map"
        val opponentMap = "opponent-map"

        val createMatchRequestDto =
            CreateMatchRequestDto(
                mode = MatchModeDto.MANUAL,
                opponentUsername = opponentPublicUser.username,
            )

        every { publicUserService.getPublicUserByUsername(opponentPublicUser.username) } returns
            opponentPublicUser
        every { lockedCodeService.getLockedCode(userId) } returns userCode
        every { lockedCodeService.getLockedCode(opponentId) } returns opponentCode
        every { lockedMapService.getLockedMap(userId) } returns userMap
        every { lockedMapService.getLockedMap(opponentId) } returns opponentMap
        every { gameService.createGame(any()) } returns mockk()
        every { matchRepository.save(any()) } returns mockk()
        every { gameService.sendGameRequest(any(), userCode.second, userCode.first, userMap) } returns
            Unit
        every {
            gameService.sendGameRequest(any(), opponentCode.second, opponentCode.first, opponentMap)
        } returns Unit

        matchService.createMatch(userId, createMatchRequestDto)

        verify {
            publicUserService.getPublicUserByUsername(opponentPublicUser.username)
            lockedCodeService.getLockedCode(userId)
            lockedCodeService.getLockedCode(opponentId)
            lockedMapService.getLockedMap(userId)
            lockedMapService.getLockedMap(opponentId)
            gameService.createGame(any())
            matchRepository.save(any())
            gameService.sendGameRequest(any(), userCode.second, userCode.first, opponentMap)
            gameService.sendGameRequest(any(), opponentCode.second, opponentCode.first, userMap)
        }
        confirmVerified(
            codeRevisionService, mapRevisionService, gameService, matchRepository, gameService
        )
    }

    @Test
    fun `should create auto match`() {
        val userId = UUID.randomUUID()
        val opponentId = UUID.randomUUID()
        val opponentPublicUser =
            TestAttributes.publicUser.copy(userId = opponentId, username = "opponent")

        val userCode = Pair(LanguageEnum.CPP, "user-code")
        val opponentCode = Pair(LanguageEnum.PYTHON, "opponent-code")
        val userMap = "user-map"
        val opponentMap = "opponent-map"

        val createMatchRequestDto =
            CreateMatchRequestDto(
                mode = MatchModeDto.AUTO,
                opponentUsername = opponentPublicUser.username,
            )

        every { publicUserService.getPublicUserByUsername(opponentPublicUser.username) } returns
            opponentPublicUser
        every { lockedCodeService.getLockedCode(userId) } returns userCode
        every { lockedCodeService.getLockedCode(opponentId) } returns opponentCode
        every { lockedMapService.getLockedMap(userId) } returns userMap
        every { lockedMapService.getLockedMap(opponentId) } returns opponentMap
        every { gameService.createGame(any()) } returns mockk()
        every { matchRepository.save(any()) } returns mockk()
        every { gameService.sendGameRequest(any(), userCode.second, userCode.first, userMap) } returns
            Unit
        every {
            gameService.sendGameRequest(any(), opponentCode.second, opponentCode.first, opponentMap)
        } returns Unit

        matchService.createMatch(userId, createMatchRequestDto)

        verify {
            publicUserService.getPublicUserByUsername(opponentPublicUser.username)
            lockedCodeService.getLockedCode(userId)
            lockedCodeService.getLockedCode(opponentId)
            lockedMapService.getLockedMap(userId)
            lockedMapService.getLockedMap(opponentId)
            gameService.createGame(any())
            matchRepository.save(any())
            gameService.sendGameRequest(any(), userCode.second, userCode.first, opponentMap)
            gameService.sendGameRequest(any(), opponentCode.second, opponentCode.first, userMap)
        }
        confirmVerified(
            codeRevisionService, mapRevisionService, gameService, matchRepository, gameService
        )
    }
}
