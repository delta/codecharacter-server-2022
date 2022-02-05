package delta.codecharacter.server.code.latest_code

import delta.codecharacter.dtos.CodeDto
import delta.codecharacter.dtos.LanguageDto
import delta.codecharacter.dtos.UpdateLatestCodeRequestDto
import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant

/** Service for handling the latest code. */
@Service
class LatestCodeService(@Autowired private val latestCodeRepository: LatestCodeRepository) {

    fun getLatestCode(userEntity: UserEntity): CodeDto {
        return latestCodeRepository
            .findById(userEntity)
            .orElseThrow { throw Exception("Latest code not found for user ${userEntity.id}") }
            .let { latestCode ->
                CodeDto(
                    code = latestCode.code,
                    language = LanguageDto.valueOf(latestCode.language.name),
                    lastSavedAt = latestCode.lastSavedAt
                )
            }
    }

    fun updateLatestCode(
        userEntity: UserEntity,
        updateLatestCodeRequestDto: UpdateLatestCodeRequestDto
    ) {
        latestCodeRepository.save(
            LatestCodeEntity(
                code = updateLatestCodeRequestDto.code,
                language = LanguageEnum.valueOf(updateLatestCodeRequestDto.language.name),
                user = userEntity,
                lastSavedAt = Instant.now()
            )
        )
    }
}
