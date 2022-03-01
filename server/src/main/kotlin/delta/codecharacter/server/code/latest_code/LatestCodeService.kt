package delta.codecharacter.server.code.latest_code

import delta.codecharacter.dtos.CodeDto
import delta.codecharacter.dtos.LanguageDto
import delta.codecharacter.dtos.UpdateLatestCodeRequestDto
import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.config.DefaultCodeMapConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

/** Service for handling the latest code. */
@Service
class LatestCodeService(
    @Autowired private val latestCodeRepository: LatestCodeRepository,
    @Autowired private val defaultCodeMapConfiguration: DefaultCodeMapConfiguration
) {

    fun getLatestCode(userId: UUID): CodeDto {
        return latestCodeRepository
            .findById(userId)
            .orElse(
                LatestCodeEntity(
                    userId,
                    code = defaultCodeMapConfiguration.defaultCode,
                    language = defaultCodeMapConfiguration.defaultLanguage,
                    lastSavedAt = Instant.MIN
                )
            )
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
