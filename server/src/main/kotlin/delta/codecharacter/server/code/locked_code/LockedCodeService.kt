package delta.codecharacter.server.code.locked_code

import delta.codecharacter.dtos.CodeTypeDto
import delta.codecharacter.dtos.UpdateLatestCodeRequestDto
import delta.codecharacter.server.code.Code
import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.config.DefaultCodeMapConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

/** Service for locked code. */
@Service
class LockedCodeService(
    @Autowired private val lockedCodeRepository: LockedCodeRepository,
    @Autowired private val defaultCodeMapConfiguration: DefaultCodeMapConfiguration
) {

    fun getLockedCode(
        userId: UUID,
        codeType: CodeTypeDto = CodeTypeDto.NORMAL
    ): Pair<LanguageEnum, String> {
        val lockedCode = HashMap<CodeTypeDto, Code>()
        lockedCode[codeType] = defaultCodeMapConfiguration.defaultLockedCode
        return lockedCodeRepository
            .findById(userId)
            .orElse(
                LockedCodeEntity(
                    userId = userId,
                    lockedCode = lockedCode,
                )
            )
            .let { code ->
                Pair(
                    code.lockedCode[codeType]?.language ?: defaultCodeMapConfiguration.defaultLanguage,
                    code.lockedCode[codeType]?.code ?: defaultCodeMapConfiguration.defaultCode
                )
            }
    }

    fun updateLockedCode(userId: UUID, updateLatestCodeRequestDto: UpdateLatestCodeRequestDto) {
        val lockedCode = HashMap<CodeTypeDto, Code>()
        lockedCode[updateLatestCodeRequestDto.codeType ?: CodeTypeDto.NORMAL] =
            Code(
                code = updateLatestCodeRequestDto.code,
                language = LanguageEnum.valueOf(updateLatestCodeRequestDto.language.name)
            )
        if (lockedCodeRepository.findById(userId).isEmpty) {
            lockedCodeRepository.save(LockedCodeEntity(userId = userId, lockedCode = lockedCode))
        } else {
            val code = lockedCodeRepository.findById(userId).get()
            val currentLockCode = code.lockedCode
            currentLockCode[updateLatestCodeRequestDto.codeType ?: CodeTypeDto.NORMAL] =
                Code(
                    code = updateLatestCodeRequestDto.code,
                    language = LanguageEnum.valueOf(updateLatestCodeRequestDto.language.name)
                )
            val updatedLockedCodeEntity =
                code.copy(
                    lockedCode = currentLockCode,
                )
            lockedCodeRepository.save(updatedLockedCodeEntity)
        }
    }
}
