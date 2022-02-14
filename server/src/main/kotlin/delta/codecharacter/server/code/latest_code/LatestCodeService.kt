package delta.codecharacter.server.code.latest_code

import delta.codecharacter.dtos.CodeDto
import delta.codecharacter.dtos.LanguageDto
import delta.codecharacter.dtos.UpdateLatestCodeRequestDto
import delta.codecharacter.server.code.LanguageEnum
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

/** Service for handling the latest code. */
@Service
class LatestCodeService(@Autowired private val latestCodeRepository: LatestCodeRepository) {

    fun getLatestCode(userId: UUID): CodeDto {
        return latestCodeRepository
            .findById(userId)
            .orElseThrow { throw Exception("Latest code not found for user $userId") }
            .let { latestCode ->
                CodeDto(
                    code = latestCode.code,
                    language = LanguageDto.valueOf(latestCode.language.name),
                    lastSavedAt = latestCode.lastSavedAt
                )
            }
    }

    fun updateLatestCode(userId: UUID, updateLatestCodeRequestDto: UpdateLatestCodeRequestDto) {
        latestCodeRepository.save(
            LatestCodeEntity(
                code = updateLatestCodeRequestDto.code,
                language = LanguageEnum.valueOf(updateLatestCodeRequestDto.language.name),
                userId = userId,
                lastSavedAt = Instant.now()
            )
        )
    }
}
