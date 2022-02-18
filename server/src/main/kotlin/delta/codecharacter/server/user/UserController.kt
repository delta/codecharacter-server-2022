package delta.codecharacter.server.user

import delta.codecharacter.core.UserApi
import delta.codecharacter.dtos.ActivateUserRequestDto
import delta.codecharacter.dtos.RatingHistoryDto
import delta.codecharacter.dtos.RegisterUserRequestDto
import delta.codecharacter.server.user.rating_history.RatingHistoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@CrossOrigin(origins = ["*"])
@RestController
class UserController(
    @Autowired private val userService: UserService,
    @Autowired private val ratingHistoryService: RatingHistoryService
) : UserApi {

    override fun register(
        @Validated registerUserRequestDto: RegisterUserRequestDto
    ): ResponseEntity<Unit> {
        userService.registerUser(registerUserRequestDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(Unit)
    }

    override fun activateUser(
        userId: UUID,
        activateUserRequestDto: ActivateUserRequestDto
    ): ResponseEntity<Unit> {
        TODO("Implement user activation")
    }

    override fun getRatingHistory(userId: UUID): ResponseEntity<List<RatingHistoryDto>> {
        val ratingHistory = ratingHistoryService.getRatingHistory(userId)
        return ResponseEntity.ok(ratingHistory)
    }
}
