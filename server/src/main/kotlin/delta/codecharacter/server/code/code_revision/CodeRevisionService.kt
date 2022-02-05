package delta.codecharacter.server.code.code_revision

import delta.codecharacter.dtos.CodeRevisionDto
import delta.codecharacter.dtos.CreateCodeRevisionRequestDto
import delta.codecharacter.dtos.LanguageDto
import delta.codecharacter.server.code.LanguageEnum
import delta.codecharacter.server.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

/** Service for code revision. */
@Service
class CodeRevisionService(@Autowired private val codeRevisionRepository: CodeRevisionRepository) {

    fun createCodeRevision(
        userEntity: UserEntity,
        createCodeRevisionRequestDto: CreateCodeRevisionRequestDto
    ) {
        val (code, language) = createCodeRevisionRequestDto
        val parentCodeRevision =
            codeRevisionRepository.findFirstByUserOrderByCreatedAtDesc(userEntity).orElse(null)
        codeRevisionRepository.save(
            CodeRevisionEntity(
                id = UUID.randomUUID(),
                code = code,
                language = LanguageEnum.valueOf(language.name),
                user = userEntity,
                parentRevision = parentCodeRevision,
                createdAt = Instant.now()
            )
        )
    }

    fun getCodeRevisions(userEntity: UserEntity): List<CodeRevisionDto> {
        return codeRevisionRepository.findAllByUserOrderByCreatedAtDesc(userEntity).map {
            CodeRevisionDto(
                id = it.id,
                code = it.code,
                language = LanguageDto.valueOf(it.language.name),
                createdAt = it.createdAt,
                parentRevision = it.parentRevision?.id
            )
        }
    }
}
