package delta.codecharacter.server.code.code_revision

import delta.codecharacter.dtos.CodeRevisionDto
import delta.codecharacter.dtos.CreateCodeRevisionRequestDto
import delta.codecharacter.dtos.LanguageDto
import delta.codecharacter.server.code.LanguageEnum
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

/** Service for code revision. */
@Service
class CodeRevisionService(@Autowired private val codeRevisionRepository: CodeRevisionRepository) {

    fun createCodeRevision(userId: UUID, createCodeRevisionRequestDto: CreateCodeRevisionRequestDto) {
        val (code, message, language) = createCodeRevisionRequestDto
        val parentCodeRevision =
            codeRevisionRepository.findFirstByUserIdOrderByCreatedAtDesc(userId).orElse(null)
        codeRevisionRepository.save(
            CodeRevisionEntity(
                id = UUID.randomUUID(),
                code = code,
                message = message,
                language = LanguageEnum.valueOf(language.name),
                userId = userId,
                parentRevision = parentCodeRevision,
                createdAt = Instant.now()
            )
        )
    }

    fun getCodeRevisions(userId: UUID): List<CodeRevisionDto> {
        return codeRevisionRepository.findAllByUserIdOrderByCreatedAtDesc(userId).map {
            CodeRevisionDto(
                id = it.id,
                code = it.code,
                message = it.message,
                language = LanguageDto.valueOf(it.language.name),
                createdAt = it.createdAt,
                parentRevision = it.parentRevision?.id
            )
        }
    }
}
