package delta.codecharacter.server.match

import delta.codecharacter.dtos.CreateMatchRequestDto
import delta.codecharacter.dtos.MatchModeDto
import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.code.code_revision.CodeRevisionService
import delta.codecharacter.server.code.locked_code.LockedCodeService
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.game.GameService
import delta.codecharacter.server.game_map.locked_map.LockedMapService
import delta.codecharacter.server.game_map.map_revision.MapRevisionService
import delta.codecharacter.server.user.UserEntity
import delta.codecharacter.server.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class MatchService(
    @Autowired private val matchRepository: MatchRepository,
    @Autowired private val gameService: GameService,
    @Autowired private val codeRevisionService: CodeRevisionService,
    @Autowired private val lockedCodeService: LockedCodeService,
    @Autowired private val mapRevisionService: MapRevisionService,
    @Autowired private val lockedMapService: LockedMapService,
    @Autowired private val userService: UserService
) {
    fun createMatch(user: UserEntity, createMatchRequestDto: CreateMatchRequestDto) {
        when (createMatchRequestDto.mode) {
            MatchModeDto.SELF -> {
                if (createMatchRequestDto.codeRevisionId == null ||
                    createMatchRequestDto.mapRevisionId == null
                ) {
                    throw CustomException(HttpStatus.BAD_REQUEST, "Revision IDs are required for self match")
                }
                val (_, code, language) =
                    codeRevisionService.getCodeRevisions(user).find {
                        it.id == createMatchRequestDto.codeRevisionId
                    }
                        ?: throw CustomException(HttpStatus.BAD_REQUEST, "Invalid revision ID")
                val map =
                    mapRevisionService
                        .getMapRevisions(user)
                        .find { it.id == createMatchRequestDto.mapRevisionId }
                        ?.map
                        ?: throw CustomException(HttpStatus.BAD_REQUEST, "Invalid revision ID")

                val matchId = UUID.randomUUID()
                val game = gameService.createGame(matchId)
                val match =
                    MatchEntity(
                        id = matchId,
                        games = setOf(game),
                        mode = MatchModeEnum.SELF,
                        verdict = MatchVerdictEnum.TIE,
                        createdAt = Instant.now(),
                        totalPoints = 0,
                        player1 = user,
                        player2 = user,
                    )
                matchRepository.save(match)
                gameService.sendGameRequest(game, code, LanguageEnum.valueOf(language.name), map)
            }
            MatchModeDto.MANUAL, MatchModeDto.AUTO -> {
                if (createMatchRequestDto.opponentId == null) {
                    throw CustomException(HttpStatus.BAD_REQUEST, "Opponent ID is required")
                }

                val (userLanguage, userCode) = lockedCodeService.getLockedCode(user)
                val userMap = lockedMapService.getLockedMap(user)

                val opponent = userService.getUserById(createMatchRequestDto.opponentId!!)
                val (opponentLanguage, opponentCode) = lockedCodeService.getLockedCode(opponent)
                val opponentMap = lockedMapService.getLockedMap(opponent)

                val matchId = UUID.randomUUID()

                val game1 = gameService.createGame(matchId)
                val game2 = gameService.createGame(matchId)
                val match =
                    MatchEntity(
                        id = matchId,
                        games = setOf(game1, game2),
                        mode = MatchModeEnum.MANUAL,
                        verdict = MatchVerdictEnum.TIE,
                        createdAt = Instant.now(),
                        totalPoints = 0,
                        player1 = user,
                        player2 = opponent,
                    )
                matchRepository.save(match)

                gameService.sendGameRequest(game1, userCode, userLanguage, opponentMap)
                gameService.sendGameRequest(game2, opponentCode, opponentLanguage, userMap)
            }
        }
    }
}
