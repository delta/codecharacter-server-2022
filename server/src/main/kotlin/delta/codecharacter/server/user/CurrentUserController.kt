package delta.codecharacter.server.user

import delta.codecharacter.core.CurrentUserApi
import delta.codecharacter.dtos.CompleteProfileRequestDto
import delta.codecharacter.dtos.CurrentUserProfileDto
import delta.codecharacter.dtos.UpdateCurrentUserProfileDto
import delta.codecharacter.dtos.UpdatePasswordRequestDto
import delta.codecharacter.server.user.public_user.PublicUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RestController

@RestController
class CurrentUserController(
    @Autowired private val userService: UserService,
    @Autowired private val publicUserService: PublicUserService
) : CurrentUserApi {
    @Secured(value = ["ROLE_USER_INCOMPLETE_PROFILE"])
    override fun completeUserProfile(
        completeProfileRequestDto: CompleteProfileRequestDto
    ): ResponseEntity<Unit> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        userService.completeUserProfile(user.id, completeProfileRequestDto)
        return ResponseEntity.ok().build()
    }

    @Secured(value = ["ROLE_USER"])
    override fun getCurrentUser(): ResponseEntity<CurrentUserProfileDto> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        return ResponseEntity.ok(publicUserService.getUserProfile(user.id, user.email))
    }

    @Secured(value = ["ROLE_USER"])
    override fun updateCurrentUser(
        updateCurrentUserProfileDto: UpdateCurrentUserProfileDto
    ): ResponseEntity<Unit> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        publicUserService.updateUserProfile(user.id, updateCurrentUserProfileDto)
        return ResponseEntity.ok().build()
    }

    @Secured(value = ["ROLE_USER"])
    override fun updatePassword(
        updatePasswordRequestDto: UpdatePasswordRequestDto
    ): ResponseEntity<Unit> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        userService.updatePassword(user.id, updatePasswordRequestDto)
        return ResponseEntity.ok().build()
    }
}
