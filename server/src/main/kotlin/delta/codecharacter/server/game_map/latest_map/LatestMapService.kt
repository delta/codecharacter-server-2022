package delta.codecharacter.server.game_map.latest_map

import delta.codecharacter.dtos.GameMapDto
import delta.codecharacter.dtos.UpdateLatestMapRequestDto
import delta.codecharacter.server.config.DefaultCodeMapConfiguration
import delta.codecharacter.server.logic.validation.MapValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

/** Service for handling the latest map. */
@Service
class LatestMapService(
    @Autowired private val latestMapRepository: LatestMapRepository,
    @Autowired private val defaultCodeMapConfiguration: DefaultCodeMapConfiguration,
    @Autowired private val mapValidator: MapValidator,
) {

    fun getLatestMap(userId: UUID): GameMapDto {
        return latestMapRepository
            .findById(userId)
            .orElse(
                LatestMapEntity(
                    userId,
                    map = defaultCodeMapConfiguration.defaultMap,
                    lastSavedAt = Instant.MIN,
                )
            )
            .let { latestMap -> GameMapDto(map = latestMap.map, lastSavedAt = latestMap.lastSavedAt) }
    }

    fun updateLatestMap(userId: UUID, updateLatestMapDto: UpdateLatestMapRequestDto) {
        mapValidator.validateMap(updateLatestMapDto.map)
        latestMapRepository.save(
            LatestMapEntity(map = updateLatestMapDto.map, userId = userId, lastSavedAt = Instant.now())
        )
    }
}
