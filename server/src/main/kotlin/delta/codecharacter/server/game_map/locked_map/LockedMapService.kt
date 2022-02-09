package delta.codecharacter.server.game_map.locked_map

import delta.codecharacter.dtos.UpdateLatestMapRequestDto
import delta.codecharacter.server.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/** Service for locked map. */
@Service
class LockedMapService(
    @Autowired private val lockedMapRepository: LockedMapRepository,
) {

    fun getLockedMap(userEntity: UserEntity): String {
        return lockedMapRepository
            .findById(userEntity)
            .orElseThrow { throw Exception("Latest code not found for user ${userEntity.id}") }
            .map
    }

    fun updateLockedMap(
        userEntity: UserEntity,
        updateLatestMapRequestDto: UpdateLatestMapRequestDto
    ) {
        lockedMapRepository.save(
            LockedMapEntity(map = updateLatestMapRequestDto.map, user = userEntity)
        )
    }
}
