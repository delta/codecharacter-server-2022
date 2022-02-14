package delta.codecharacter.server.game_map.locked_map

import delta.codecharacter.dtos.UpdateLatestMapRequestDto
import delta.codecharacter.server.logic.validation.MapValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

/** Service for locked map. */
@Service
class LockedMapService(
    @Autowired private val lockedMapRepository: LockedMapRepository,
    @Autowired private val mapValidator: MapValidator,
) {

    fun getLockedMap(userId: UUID): String {
        return lockedMapRepository
            .findById(userId)
            .orElseThrow { throw Exception("Latest code not found for user $userId") }
            .map
    }

    fun updateLockedMap(userId: UUID, updateLatestMapRequestDto: UpdateLatestMapRequestDto) {
        mapValidator.validateMap(updateLatestMapRequestDto.map)
        lockedMapRepository.save(LockedMapEntity(map = updateLatestMapRequestDto.map, userId = userId))
    }
}
