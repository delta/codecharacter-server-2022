package delta.codecharacter.server.game_map

import delta.codecharacter.core.MapApi
import delta.codecharacter.dtos.CreateMapRevisionRequestDto
import delta.codecharacter.dtos.GameMapDto
import delta.codecharacter.dtos.GameMapRevisionDto
import delta.codecharacter.dtos.UpdateLatestMapRequestDto
import delta.codecharacter.server.exception.CustomException
import delta.codecharacter.server.game_map.latest_map.LatestMapService
import delta.codecharacter.server.game_map.locked_map.LockedMapService
import delta.codecharacter.server.game_map.map_revision.MapRevisionService
import delta.codecharacter.server.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RestController

@RestController
class GameMapController(
    @Autowired private val mapRevisionService: MapRevisionService,
    @Autowired private val latestMapService: LatestMapService,
    @Autowired private val lockedMapService: LockedMapService
) : MapApi {

    @Secured(value = ["ROLE_USER"])
    override fun createMapRevision(
        createMapRevisionRequestDto: CreateMapRevisionRequestDto
    ): ResponseEntity<Unit> {
        throw CustomException(HttpStatus.BAD_REQUEST, "The game has ended!")
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        mapRevisionService.createMapRevision(user.id, createMapRevisionRequestDto)
        return ResponseEntity.ok().build()
    }

    @Secured(value = ["ROLE_USER"])
    override fun getMapRevisions(): ResponseEntity<List<GameMapRevisionDto>> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        return ResponseEntity.ok(mapRevisionService.getMapRevisions(user.id))
    }

    @Secured(value = ["ROLE_USER"])
    override fun getLatestMap(): ResponseEntity<GameMapDto> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        return ResponseEntity.ok(latestMapService.getLatestMap(user.id))
    }

    @Secured(value = ["ROLE_USER"])
    override fun updateLatestMap(
        updateLatestMapRequestDto: UpdateLatestMapRequestDto
    ): ResponseEntity<Unit> {
        throw CustomException(HttpStatus.BAD_REQUEST, "The game has ended!")
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        latestMapService.updateLatestMap(user.id, updateLatestMapRequestDto)
        if (updateLatestMapRequestDto.lock == true) {
            lockedMapService.updateLockedMap(user.id, updateLatestMapRequestDto)
        }
        return ResponseEntity.ok().build()
    }
}
