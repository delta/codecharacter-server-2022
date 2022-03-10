package delta.codecharacter.server.code

import delta.codecharacter.core.CodeApi
import delta.codecharacter.dtos.CodeDto
import delta.codecharacter.dtos.CodeRevisionDto
import delta.codecharacter.dtos.CreateCodeRevisionRequestDto
import delta.codecharacter.dtos.UpdateLatestCodeRequestDto
import delta.codecharacter.server.code.code_revision.CodeRevisionService
import delta.codecharacter.server.code.latest_code.LatestCodeService
import delta.codecharacter.server.code.locked_code.LockedCodeService
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RestController

@RestController
class CodeController(
    @Autowired private val codeRevisionService: CodeRevisionService,
    @Autowired private val latestCodeService: LatestCodeService,
    @Autowired private val lockedCodeService: LockedCodeService
) : CodeApi {

    @Secured(value = ["ROLE_USER"])
    override fun createCodeRevision(
        createCodeRevisionRequestDto: CreateCodeRevisionRequestDto
    ): ResponseEntity<Unit> {
        throw CustomException(HttpStatus.BAD_REQUEST, "The game has ended!")
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        codeRevisionService.createCodeRevision(user.id, createCodeRevisionRequestDto)
        return ResponseEntity.ok().build()
    }

    @Secured(value = ["ROLE_USER"])
    override fun getCodeRevisions(): ResponseEntity<List<CodeRevisionDto>> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        return ResponseEntity.ok(codeRevisionService.getCodeRevisions(user.id))
    }

    @Secured(value = ["ROLE_USER"])
    override fun getLatestCode(): ResponseEntity<CodeDto> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        return ResponseEntity.ok(latestCodeService.getLatestCode(user.id))
    }

    @Secured(value = ["ROLE_USER"])
    override fun updateLatestCode(
        updateLatestCodeRequestDto: UpdateLatestCodeRequestDto
    ): ResponseEntity<Unit> {
        throw CustomException(HttpStatus.BAD_REQUEST, "The game has ended!")
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        latestCodeService.updateLatestCode(user.id, updateLatestCodeRequestDto)
        if (updateLatestCodeRequestDto.lock == true) {
            lockedCodeService.updateLockedCode(user.id, updateLatestCodeRequestDto)
        }
        return ResponseEntity.ok().build()
    }
}
