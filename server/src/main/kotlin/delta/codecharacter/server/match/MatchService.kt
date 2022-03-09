package delta.codecharacter.server.match

import com.fasterxml.jackson.databind.ObjectMapper
import delta.codecharacter.dtos.CreateMatchRequestDto
import delta.codecharacter.dtos.GameDto
import delta.codecharacter.dtos.GameStatusDto
import delta.codecharacter.dtos.MatchDto
import delta.codecharacter.dtos.MatchModeDto
import delta.codecharacter.dtos.PublicUserDto
import delta.codecharacter.dtos.VerdictDto
import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.code.code_revision.CodeRevisionService
import delta.codecharacter.server.code.latest_code.LatestCodeService
import delta.codecharacter.server.code.locked_code.LockedCodeService
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.game.GameService
import delta.codecharacter.server.game.GameStatusEnum
import delta.codecharacter.server.game_map.latest_map.LatestMapService
import delta.codecharacter.server.game_map.locked_map.LockedMapService
import delta.codecharacter.server.game_map.map_revision.MapRevisionService
import delta.codecharacter.server.logic.verdict.VerdictAlgorithm
import delta.codecharacter.server.notifications.NotificationService
import delta.codecharacter.server.user.public_user.PublicUserService
import delta.codecharacter.server.user.rating_history.RatingHistoryService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Service
class MatchService(
    @Autowired private val matchRepository: MatchRepository,
    @Autowired private val autoMatchRepository: AutoMatchRepository,
    @Autowired private val gameService: GameService,
    @Autowired private val latestCodeService: LatestCodeService,
    @Autowired private val codeRevisionService: CodeRevisionService,
    @Autowired private val lockedCodeService: LockedCodeService,
    @Autowired private val latestMapService: LatestMapService,
    @Autowired private val mapRevisionService: MapRevisionService,
    @Autowired private val lockedMapService: LockedMapService,
    @Autowired private val publicUserService: PublicUserService,
    @Autowired private val verdictAlgorithm: VerdictAlgorithm,
    @Autowired private val ratingHistoryService: RatingHistoryService,
    @Autowired private val notificationService: NotificationService,
    @Autowired private val jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder,
    @Autowired private val simpMessagingTemplate: SimpMessagingTemplate,
) {
    private var mapper: ObjectMapper = jackson2ObjectMapperBuilder.build()

    private val logger: Logger = LoggerFactory.getLogger(MatchService::class.java)

    private fun createSelfMatch(userId: UUID, codeRevisionId: UUID?, mapRevisionId: UUID?): UUID {
        val code: String
        val language: LanguageEnum
        if (codeRevisionId == null) {
            val latestCode = latestCodeService.getLatestCode(userId)
            code = latestCode.code
            language = LanguageEnum.valueOf(latestCode.language.name)
        } else {
            val codeRevision =
                codeRevisionService.getCodeRevisions(userId).find { it.id == codeRevisionId }
                    ?: throw CustomException(HttpStatus.BAD_REQUEST, "Invalid revision ID")
            code = codeRevision.code
            language = LanguageEnum.valueOf(codeRevision.language.name)
        }

        val map: String =
            if (mapRevisionId == null) {
                val latestMap = latestMapService.getLatestMap(userId)
                latestMap.map
            } else {
                val mapRevision =
                    mapRevisionService.getMapRevisions(userId).find { it.id == mapRevisionId }
                        ?: throw CustomException(HttpStatus.BAD_REQUEST, "Invalid revision ID")
                mapRevision.map
            }

        val matchId = UUID.randomUUID()
        val game = gameService.createGame(matchId)
        val publicUser = publicUserService.getPublicUser(userId)
        val match =
            MatchEntity(
                id = matchId,
                games = listOf(game),
                mode = MatchModeEnum.SELF,
                verdict = MatchVerdictEnum.TIE,
                createdAt = Instant.now(),
                totalPoints = 0,
                player1 = publicUser,
                player2 = publicUser,
            )
        matchRepository.save(match)
        gameService.sendGameRequest(game, code, LanguageEnum.valueOf(language.name), map)

        return matchId
    }

    fun createDualMatch(mode: MatchModeEnum, userId: UUID, opponentUsername: String): UUID {
        val publicUser = publicUserService.getPublicUser(userId)
        val publicOpponent = publicUserService.getPublicUserByUsername(opponentUsername)
        val opponentId = publicOpponent.userId

        if (userId == opponentId) {
            throw CustomException(HttpStatus.BAD_REQUEST, "You cannot play against yourself")
        }

        val (userLanguage, userCode) = lockedCodeService.getLockedCode(userId)
        val userMap = lockedMapService.getLockedMap(userId)

        val (opponentLanguage, opponentCode) = lockedCodeService.getLockedCode(opponentId)
        val opponentMap = lockedMapService.getLockedMap(opponentId)

        val matchId = UUID.randomUUID()

        val game1 = gameService.createGame(matchId)
        val game2 = gameService.createGame(matchId)

        val match =
            MatchEntity(
                id = matchId,
                games = listOf(game1, game2),
                mode = mode,
                verdict = MatchVerdictEnum.TIE,
                createdAt = Instant.now(),
                totalPoints = 0,
                player1 = publicUser,
                player2 = publicOpponent,
            )
        matchRepository.save(match)

        gameService.sendGameRequest(game1, userCode, userLanguage, opponentMap)
        gameService.sendGameRequest(game2, opponentCode, opponentLanguage, userMap)

        return matchId
    }

    fun createMatch(userId: UUID, createMatchRequestDto: CreateMatchRequestDto) {
        when (createMatchRequestDto.mode) {
            MatchModeDto.SELF -> {
                val (_, _, mapRevisionId, codeRevisionId) = createMatchRequestDto
                createSelfMatch(userId, codeRevisionId, mapRevisionId)
            }
            MatchModeDto.MANUAL -> {
                if (createMatchRequestDto.opponentUsername == null) {
                    throw CustomException(HttpStatus.BAD_REQUEST, "Opponent ID is required")
                }
                createDualMatch(MatchModeEnum.MANUAL, userId, createMatchRequestDto.opponentUsername!!)
            }
            MatchModeDto.AUTO -> {
                throw CustomException(HttpStatus.BAD_REQUEST, "Users cannot create auto match")
            }
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    fun createAutoMatches() {
        logger.info("Starting auto-matches...")
        autoMatchRepository.deleteAll()
        val top15Users = publicUserService.getTop15()
        val userIds = top15Users.map { it.userId }
        val userNames = top15Users.map { it.username }
        userIds.forEachIndexed { i, userId ->
            run {
                for (j in i + 1 until userIds.size) {
                    val opponentUsername = userNames[j]
                    val matchId = createDualMatch(MatchModeEnum.AUTO, userId, opponentUsername)
                    autoMatchRepository.save(AutoMatchEntity(matchId, 0))
                }
            }
        }
    }

    private fun mapMatchEntitiesToDtos(matchEntities: List<MatchEntity>): List<MatchDto> {
        return matchEntities.map { matchEntity ->
            MatchDto(
                id = matchEntity.id,
                matchMode = MatchModeDto.valueOf(matchEntity.mode.name),
                matchVerdict = VerdictDto.valueOf(matchEntity.verdict.name),
                createdAt = matchEntity.createdAt,
                games =
                matchEntity
                    .games
                    .map { gameEntity ->
                        GameDto(
                            id = gameEntity.id,
                            destruction = BigDecimal(gameEntity.destruction),
                            coinsUsed = gameEntity.coinsUsed,
                            status = GameStatusDto.valueOf(gameEntity.status.name),
                        )
                    }
                    .toSet(),
                user1 =
                PublicUserDto(
                    username = matchEntity.player1.username,
                    name = matchEntity.player1.name,
                    country = matchEntity.player1.country,
                    college = matchEntity.player1.college,
                    avatarId = matchEntity.player1.avatarId,
                ),
                user2 =
                PublicUserDto(
                    username = matchEntity.player2.username,
                    name = matchEntity.player2.name,
                    country = matchEntity.player2.country,
                    college = matchEntity.player2.college,
                    avatarId = matchEntity.player2.avatarId,
                ),
            )
        }
    }

    fun getTopMatches(): List<MatchDto> {
        val matches = matchRepository.findTop10ByOrderByTotalPointsDesc()
        return mapMatchEntitiesToDtos(matches)
    }

    fun getUserMatches(userId: UUID): List<MatchDto> {
        val publicUser = publicUserService.getPublicUser(userId)
        val matches =
            matchRepository.findTop100ByPlayer1OrModeOrderByCreatedAtDesc(
                publicUser, MatchModeEnum.AUTO
            )
        return mapMatchEntitiesToDtos(matches)
    }

    @RabbitListener(queues = ["gameStatusUpdateQueue"], ackMode = "AUTO")
    fun receiveGameResult(gameStatusUpdateJson: String) {
        val updatedGame = gameService.updateGameStatus(gameStatusUpdateJson)

        val match = matchRepository.findById(updatedGame.matchId).get()
        if (match.mode != MatchModeEnum.AUTO && match.games.first().id == updatedGame.id) {
            simpMessagingTemplate.convertAndSend(
                "/updates/${match.player1.userId}",
                mapper.writeValueAsString(
                    GameDto(
                        id = updatedGame.id,
                        destruction = BigDecimal(updatedGame.destruction),
                        coinsUsed = updatedGame.coinsUsed,
                        status = GameStatusDto.valueOf(updatedGame.status.name),
                    )
                )
            )
        }
        if (match.mode != MatchModeEnum.SELF &&
            match.games.all { game ->
                game.status == GameStatusEnum.EXECUTED || game.status == GameStatusEnum.EXECUTE_ERROR
            }
        ) {
            if (match.mode == MatchModeEnum.AUTO) {
                if (match.games.any { game -> game.status == GameStatusEnum.EXECUTE_ERROR }) {
                    val autoMatch = autoMatchRepository.findById(match.id).get()
                    if (autoMatch.tries < 3) {
                        autoMatchRepository.delete(autoMatch)
                        val matchId =
                            createDualMatch(MatchModeEnum.AUTO, match.player1.userId, match.player2.username)
                        autoMatchRepository.save(AutoMatchEntity(matchId, autoMatch.tries + 1))
                        return
                    }
                }
            }

            val player1Game = match.games.first()
            val player2Game = match.games.last()
            val verdict =
                verdictAlgorithm.getVerdict(
                    player1Game.status == GameStatusEnum.EXECUTE_ERROR,
                    player1Game.coinsUsed,
                    player1Game.destruction,
                    player2Game.status == GameStatusEnum.EXECUTE_ERROR,
                    player2Game.coinsUsed,
                    player2Game.destruction
                )
            val finishedMatch = match.copy(verdict = verdict)

            var userRating = match.player1.rating
            var opponentRating = match.player2.rating

            if (match.mode == MatchModeEnum.MANUAL) {
                val isUserInTop15 = publicUserService.isInTop15(match.player1.userId)
                val isOpponentInTop15 = publicUserService.isInTop15(match.player2.userId)
                val newRating =
                    ratingHistoryService.updateRating(
                        match.player1.userId,
                        match.player2.userId,
                        verdict,
                        isUserInTop15,
                        isOpponentInTop15
                    )
                userRating = newRating.first
                opponentRating = newRating.second

                notificationService.sendNotification(
                    match.player1.userId,
                    "Match Result",
                    "${
                    when (verdict) {
                        MatchVerdictEnum.PLAYER1 -> "Won"
                        MatchVerdictEnum.PLAYER2 -> "Lost"
                        MatchVerdictEnum.TIE -> "Tied"
                    }
                    } against ${match.player2.username}",
                )
            }

            publicUserService.updatePublicRating(
                userId = match.player1.userId,
                isInitiator = true,
                verdict = verdict,
                newRating = userRating
            )
            publicUserService.updatePublicRating(
                userId = match.player2.userId,
                isInitiator = false,
                verdict = verdict,
                newRating = opponentRating
            )

            matchRepository.save(finishedMatch)

            if (match.mode == MatchModeEnum.AUTO) {
                if (autoMatchRepository.findAll().all { autoMatch ->
                    matchRepository.findById(autoMatch.matchId).get().games.all { game ->
                        game.status == GameStatusEnum.EXECUTED || game.status == GameStatusEnum.EXECUTE_ERROR
                    }
                }
                ) {
                    logger.info("All matches are executed")
                    val matches = matchRepository.findByIdIn(autoMatchRepository.findAll().map { it.matchId })
                    val userIds =
                        matches.map { it.player1.userId }.toSet() + matches.map { it.player2.userId }.toSet()
                    val newRatings =
                        ratingHistoryService.updateAndGetAutoMatchRatings(userIds.toList(), matches)
                    newRatings.forEach { (userId, newRating) ->
                        publicUserService.updatePublicRating(userId = userId, newRating = newRating.rating)
                    }
                }
            }
        }
    }
}
