package delta.codecharacter.server.game.queue.entities

import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.params.GameParameters
import java.util.UUID

data class GameRequestEntity(
    val gameId: UUID,
    val parameters: GameParameters,
    val sourceCode: String,
    val language: LanguageEnum,
    val map: String,
)
