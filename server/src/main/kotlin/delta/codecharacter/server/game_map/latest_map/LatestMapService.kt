package delta.codecharacter.server.game_map.latest_map

import delta.codecharacter.dtos.GameMapDto
import delta.codecharacter.dtos.UpdateLatestMapRequestDto
import delta.codecharacter.server.user.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant

/** Service for handling the latest map. */
@Service
class LatestMapService(@Autowired private val latestMapRepository: LatestMapRepository) {

    fun getLatestMap(userEntity: UserEntity): GameMapDto {
        return latestMapRepository
            .findById(userEntity)
            .orElseThrow { throw Exception("Latest code not found for user ${userEntity.id}") }
            .let { latestMap -> GameMapDto(map = latestMap.map, lastSavedAt = latestMap.lastSavedAt) }
    }

    fun updateLatestMap(userEntity: UserEntity, updateLatestMapDto: UpdateLatestMapRequestDto) {
        latestMapRepository.save(
            LatestMapEntity(
                map = updateLatestMapDto.map, user = userEntity, lastSavedAt = Instant.now()
            )
        )
    }
}
