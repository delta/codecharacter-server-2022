package delta.codecharacter.server.match

import com.fasterxml.jackson.databind.ObjectMapper
import delta.codecharacter.dtos.ChallengeTypeDto
import delta.codecharacter.dtos.CreateMatchRequestDto
import delta.codecharacter.dtos.DailyChallengeMatchRequestDto
import delta.codecharacter.dtos.GameDto
import delta.codecharacter.dtos.GameStatusDto
import delta.codecharacter.dtos.MatchDto
import delta.codecharacter.dtos.MatchModeDto
import delta.codecharacter.dtos.PublicUserDto
import delta.codecharacter.dtos.TierTypeDto
import delta.codecharacter.dtos.VerdictDto
import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.code.code_revision.CodeRevisionService
import delta.codecharacter.server.code.latest_code.LatestCodeService
import delta.codecharacter.server.code.locked_code.LockedCodeService
import delta.codecharacter.server.daily_challenge.DailyChallengeService
import delta.codecharacter.server.daily_challenge.match.DailyChallengeMatchEntity
import delta.codecharacter.server.daily_challenge.match.DailyChallengeMatchRepository
import delta.codecharacter.server.daily_challenge.match.DailyChallengeMatchVerdictEnum
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.game.GameService
import delta.codecharacter.server.game.GameStatusEnum
import delta.codecharacter.server.game_map.latest_map.LatestMapService
import delta.codecharacter.server.game_map.locked_map.LockedMapService
import delta.codecharacter.server.game_map.map_revision.MapRevisionService
import delta.codecharacter.server.logic.validation.MapValidator
import delta.codecharacter.server.logic.verdict.VerdictAlgorithm
import delta.codecharacter.server.notifications.NotificationService
import delta.codecharacter.server.user.public_user.PublicUserService
import delta.codecharacter.server.user.rating_history.RatingHistoryService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Duration
import java.time.Instant
import java.util.UUID

@Service
class MatchService(
    @Autowired private val matchRepository: MatchRepository,
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
    @Autowired private val dailyChallengeService: DailyChallengeService,
    @Autowired private val dailyChallengeMatchRepository: DailyChallengeMatchRepository,
    @Autowired private val jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder,
    @Autowired private val simpMessagingTemplate: SimpMessagingTemplate,
    @Autowired private val mapValidator: MapValidator
) {
    private var mapper: ObjectMapper = jackson2ObjectMapperBuilder.build()

    private fun createSelfMatch(userId: UUID, codeRevisionId: UUID?, mapRevisionId: UUID?) {
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
    }

    fun createDualMatch(userId: UUID, opponentUsername: String) {
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
                mode = MatchModeEnum.MANUAL,
                verdict = MatchVerdictEnum.TIE,
                createdAt = Instant.now(),
                totalPoints = 0,
                player1 = publicUser,
                player2 = publicOpponent,
            )
        matchRepository.save(match)

        gameService.sendGameRequest(game1, userCode, userLanguage, opponentMap)
        gameService.sendGameRequest(game2, opponentCode, opponentLanguage, userMap)
    }

    fun createDCMatch(userId: UUID, dailyChallengeMatchRequestDto: DailyChallengeMatchRequestDto) {
        val (_, chall, challType, _, completionStatus) =
            dailyChallengeService.getDailyChallengeByDateForUser(userId)
        if (completionStatus != null && completionStatus) {
            throw CustomException(
                HttpStatus.BAD_REQUEST, "You have already completed your daily challenge"
            )
        }
        val dc = dailyChallengeService.getDailyChallengeByDate()
        val (value, _) = dailyChallengeMatchRequestDto
        val language: LanguageEnum
        val map: String
        val code: String
        when (challType) {
            ChallengeTypeDto.CODE -> { // code as question and map as answer
                mapValidator.validateMap(value)
                code = chall.cpp.toString()
                language = LanguageEnum.CPP
                map = value
            }
            ChallengeTypeDto.MAP -> {
                map = dc.map
                language = LanguageEnum.valueOf(dailyChallengeMatchRequestDto.language.toString())
                code = value
            }
        }
        val matchId = UUID.randomUUID()
        val game = gameService.createGame(matchId)
        val user = publicUserService.getPublicUser(userId)
        val match =
            DailyChallengeMatchEntity(
                id = matchId,
                verdict = DailyChallengeMatchVerdictEnum.STARTED,
                createdAt = Instant.now(),
                user = user,
                game = game
            )
        dailyChallengeMatchRepository.save(match)
        gameService.sendGameRequest(game, code, language, map)
    }
    fun createMatch(userId: UUID, createMatchRequestDto: CreateMatchRequestDto) {
        when (createMatchRequestDto.mode) {
            MatchModeDto.SELF -> {
                val (_, _, mapRevisionId, codeRevisionId) = createMatchRequestDto
                createSelfMatch(userId, codeRevisionId, mapRevisionId)
            }
            MatchModeDto.MANUAL, MatchModeDto.AUTO -> {
                if (createMatchRequestDto.opponentUsername == null) {
                    throw CustomException(HttpStatus.BAD_REQUEST, "Opponent ID is required")
                }
                createDualMatch(userId, createMatchRequestDto.opponentUsername!!)
            }
            else -> {
                throw CustomException(HttpStatus.BAD_REQUEST, "MatchMode Is Not Correct")
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
                    tier = TierTypeDto.valueOf(matchEntity.player1.tier.name),
                    country = matchEntity.player1.country,
                    college = matchEntity.player1.college,
                    avatarId = matchEntity.player1.avatarId,
                ),
                user2 =
                PublicUserDto(
                    username = matchEntity.player2.username,
                    name = matchEntity.player2.name,
                    tier = TierTypeDto.valueOf(matchEntity.player2.tier.name),
                    country = matchEntity.player2.country,
                    college = matchEntity.player2.college,
                    avatarId = matchEntity.player2.avatarId,
                ),
            )
        }
    }

    private fun mapDailyChallengeMatchEntitiesToDtos(
        dailyChallengeMatchEntities: List<DailyChallengeMatchEntity>
    ): List<MatchDto> {
        return dailyChallengeMatchEntities.map { entity ->
            MatchDto(
                id = entity.id,
                matchMode = MatchModeDto.valueOf("DAILYCHALLENGE"),
                matchVerdict = VerdictDto.valueOf(entity.verdict.name),
                createdAt = entity.createdAt,
                games =
                setOf(
                    GameDto(
                        id = entity.game.id,
                        destruction = BigDecimal(entity.game.destruction),
                        coinsUsed = entity.game.coinsUsed,
                        status = GameStatusDto.valueOf(entity.game.status.name)
                    )
                ),
                user1 =
                PublicUserDto(
                    username = entity.user.username,
                    name = entity.user.name,
                    tier = TierTypeDto.valueOf(entity.user.tier.name),
                    country = entity.user.country,
                    college = entity.user.college,
                    avatarId = entity.user.avatarId,
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
        val matches = matchRepository.findByPlayer1OrderByCreatedAtDesc(publicUser)
        val dcMatches =
            dailyChallengeMatchRepository.findByUserOrderByCreatedAtDesc(publicUser).takeWhile {
                Duration.between(it.createdAt, Instant.now()).toHours() < 24 &&
                    it.verdict != DailyChallengeMatchVerdictEnum.STARTED
            }
        return mapDailyChallengeMatchEntitiesToDtos(dcMatches) + mapMatchEntitiesToDtos(matches)
    }

    @RabbitListener(queues = ["gameStatusUpdateQueue"], ackMode = "AUTO")
    fun receiveGameResult(gameStatusUpdateJson: String) {
        val updatedGame = gameService.updateGameStatus(gameStatusUpdateJson)
        val matchId = updatedGame.matchId
        if (matchRepository.findById(matchId).isPresent) {
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
                val (newUserRating, newOpponentRating) =
                    ratingHistoryService.updateRating(match.player1.userId, match.player2.userId, verdict)

                publicUserService.updatePublicRating(
                    userId = match.player1.userId,
                    isInitiator = true,
                    verdict = verdict,
                    newRating = newUserRating
                )
                publicUserService.updatePublicRating(
                    userId = match.player2.userId,
                    isInitiator = false,
                    verdict = verdict,
                    newRating = newOpponentRating
                )

                if (match.mode == MatchModeEnum.MANUAL) {
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

                matchRepository.save(finishedMatch)
            }
        } else if (dailyChallengeMatchRepository.findById(matchId).isPresent) {
            val match = dailyChallengeMatchRepository.findById(matchId).get()
            simpMessagingTemplate.convertAndSend(
                "/updates/${match.user.userId}",
                mapper.writeValueAsString(
                    GameDto(
                        id = updatedGame.id,
                        destruction = BigDecimal(updatedGame.destruction),
                        coinsUsed = updatedGame.coinsUsed,
                        status = GameStatusDto.valueOf(updatedGame.status.name),
                    )
                )
            )
            if (updatedGame.status != GameStatusEnum.EXECUTING) {
                val updatedMatch =
                    match.copy(
                        verdict =
                        dailyChallengeService.completeDailyChallenge(updatedGame, match.user.userId)
                    )
                notificationService.sendNotification(
                    match.user.userId,
                    title = "Daily Challenge Results",
                    content =
                    when (updatedMatch.verdict) {
                        DailyChallengeMatchVerdictEnum.SUCCESS -> "Successfully completed challenge"
                        DailyChallengeMatchVerdictEnum.FAILURE -> "Failed to complete challenge"
                        else -> {
                            "Some error occurred. Try again!"
                        }
                    }
                )
                dailyChallengeMatchRepository.save(updatedMatch)
            }
        }
    }
}
