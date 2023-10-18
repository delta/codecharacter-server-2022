package delta.codecharacter.server.code.latest_code

import delta.codecharacter.dtos.CodeDto
import delta.codecharacter.dtos.CodeTypeDto
import delta.codecharacter.dtos.LanguageDto
import delta.codecharacter.dtos.UpdateLatestCodeRequestDto
import delta.codecharacter.server.code.Code
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

    fun getLatestCode(userId: UUID, codeType: CodeTypeDto = CodeTypeDto.NORMAL): CodeDto {
        val latestCode = HashMap<CodeTypeDto, Code>()
        latestCode[codeType] = defaultCodeMapConfiguration.defaultLatestCode
        val code: CodeDto =
            latestCodeRepository
                .findById(userId)
                .orElse(
                    LatestCodeEntity(
                        userId = userId,
                        latestCode = latestCode,
                    )
                )
                .let { code ->
                    CodeDto(
                        code = code.latestCode[codeType]?.code ?: defaultCodeMapConfiguration.defaultCode,
                        language =
                        LanguageDto.valueOf(
                            code.latestCode[codeType]?.language?.name
                                ?: defaultCodeMapConfiguration.defaultLanguage.name
                        ),
                        lastSavedAt = code.latestCode[codeType]?.lastSavedAt ?: Instant.MIN,
                    )
                }

        return code
    }

    fun updateLatestCode(userId: UUID, updateLatestCodeRequestDto: UpdateLatestCodeRequestDto) {
        val latestCode = HashMap<CodeTypeDto, Code>()
        latestCode[updateLatestCodeRequestDto.codeType ?: CodeTypeDto.NORMAL] =
            Code(
                code = updateLatestCodeRequestDto.code,
                language = LanguageEnum.valueOf(updateLatestCodeRequestDto.language.name),
                lastSavedAt = Instant.now()
            )
        if (latestCodeRepository.findById(userId).isEmpty) {
            latestCodeRepository.save(
                LatestCodeEntity(
                    latestCode = latestCode,
                    userId = userId,
                )
            )
        } else {
            val code = latestCodeRepository.findById(userId).get()
            val currentLatestCode = code.latestCode
            currentLatestCode[updateLatestCodeRequestDto.codeType ?: CodeTypeDto.NORMAL] =
                Code(
                    code = updateLatestCodeRequestDto.code,
                    language = LanguageEnum.valueOf(updateLatestCodeRequestDto.language.name),
                    lastSavedAt = Instant.now()
                )
            val updateCodeEntity =
                code.copy(
                    latestCode = currentLatestCode,
                )
            latestCodeRepository.save(updateCodeEntity)
        }
    }
}
