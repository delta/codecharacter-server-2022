package delta.codecharacter.server.code.locked_code

import delta.codecharacter.dtos.UpdateLatestCodeRequestDto
import delta.codecharacter.server.code.LanguageEnum
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

/** Service for locked code. */
@Service
class LockedCodeService(
    @Autowired private val lockedCodeRepository: LockedCodeRepository,
) {

    fun getLockedCode(userId: UUID): Pair<LanguageEnum, String> {
        return lockedCodeRepository
            .findById(userId)
            .orElseThrow { throw Exception("Locked code not found for user $userId") }
            .let { Pair(it.language, it.code) }
    }

    fun updateLockedCode(userId: UUID, updateLatestCodeRequestDto: UpdateLatestCodeRequestDto) {
        lockedCodeRepository.save(
            LockedCodeEntity(
                code = updateLatestCodeRequestDto.code,
                language = LanguageEnum.valueOf(updateLatestCodeRequestDto.language.name),
                userId = userId
            )
        )
    }
}
