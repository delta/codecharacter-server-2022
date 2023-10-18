package delta.codecharacter.server.game_map

import delta.codecharacter.core.MapApi
import delta.codecharacter.dtos.CreateMapRevisionRequestDto
import delta.codecharacter.dtos.GameMapDto
import delta.codecharacter.dtos.GameMapRevisionDto
import delta.codecharacter.dtos.GameMapTypeDto
import delta.codecharacter.dtos.MapCommitByCommitIdResponseDto
import delta.codecharacter.dtos.UpdateLatestMapRequestDto
import delta.codecharacter.server.game_map.latest_map.LatestMapService
import delta.codecharacter.server.game_map.locked_map.LockedMapService
import delta.codecharacter.server.game_map.map_revision.MapRevisionService
import delta.codecharacter.server.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

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
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        mapRevisionService.createMapRevision(user.id, createMapRevisionRequestDto)
        return ResponseEntity.ok().build()
    }

    @Secured(value = ["ROLE_USER"])
    override fun getLatestMap(type: GameMapTypeDto): ResponseEntity<GameMapDto> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        return ResponseEntity.ok(latestMapService.getLatestMap(user.id, type))
    }

    @Secured(value = ["ROLE_USER"])
    override fun getMapRevisions(type: GameMapTypeDto): ResponseEntity<List<GameMapRevisionDto>> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        return ResponseEntity.ok(mapRevisionService.getMapRevisions(user.id, type))
    }
    @Secured(value = ["ROLE_USER"])
    override fun updateLatestMap(
        updateLatestMapRequestDto: UpdateLatestMapRequestDto
    ): ResponseEntity<Unit> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        latestMapService.updateLatestMap(user.id, updateLatestMapRequestDto)
        if (updateLatestMapRequestDto.lock == true) {
            lockedMapService.updateLockedMap(user.id, updateLatestMapRequestDto)
        }
        return ResponseEntity.ok().build()
    }

    @Secured(value = ["ROLE_USER"])
    override fun getMapByCommitID(commitId: UUID): ResponseEntity<MapCommitByCommitIdResponseDto> {
        val user = SecurityContextHolder.getContext().authentication.principal as UserEntity
        return ResponseEntity.ok(
            mapRevisionService.getMapRevisionByCommitId(user.id, commitId = commitId)
        )
    }
}
