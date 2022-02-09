package delta.codecharacter.server.code.locked_code

import delta.codecharacter.dtos.UpdateLatestCodeRequestDto
import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/** Service for locked code. */
@Service
class LockedCodeService(
    @Autowired private val lockedCodeRepository: LockedCodeRepository,
) {

    fun getLockedCode(userEntity: UserEntity): Pair<LanguageEnum, String> {
        return lockedCodeRepository
            .findById(userEntity)
            .orElseThrow { throw Exception("Locked code not found for user ${userEntity.id}") }
            .let { Pair(it.language, it.code) }
    }

    fun updateLockedCode(
        userEntity: UserEntity,
        updateLatestCodeRequestDto: UpdateLatestCodeRequestDto
    ) {
        lockedCodeRepository.save(
            LockedCodeEntity(
                code = updateLatestCodeRequestDto.code,
                language = LanguageEnum.valueOf(updateLatestCodeRequestDto.language.name),
                user = userEntity
            )
        )
    }
}
