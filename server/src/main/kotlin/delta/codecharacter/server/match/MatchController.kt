package delta.codecharacter.server.match

import delta.codecharacter.core.MatchApi
import delta.codecharacter.dtos.CreateMatchRequestDto
import delta.codecharacter.dtos.MatchDto
import delta.codecharacter.server.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RestController

@RestController
class MatchController(@Autowired private val matchService: MatchService) : MatchApi {

    @Secured("ROLE_USER")
    override fun createMatch(createMatchRequestDto: CreateMatchRequestDto): ResponseEntity<Unit> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        matchService.createMatch(user.id, createMatchRequestDto)
        return ResponseEntity.ok().build()
    }

    @Secured("ROLE_USER")
    override fun getTopMatches(): ResponseEntity<List<MatchDto>> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        return ResponseEntity.ok(matchService.getTopMatches(user.id))
    }

    @Secured("ROLE_USER")
    override fun getUserMatches(): ResponseEntity<List<MatchDto>> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        return ResponseEntity.ok(matchService.getUserMatches(user.id))
    }
}
