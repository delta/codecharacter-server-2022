package delta.codecharacter.server.game_map.locked_map

import delta.codecharacter.dtos.GameMapTypeDto
import delta.codecharacter.dtos.UpdateLatestMapRequestDto
import delta.codecharacter.server.config.DefaultCodeMapConfiguration
import delta.codecharacter.server.game_map.GameMap
import delta.codecharacter.server.logic.validation.MapValidator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

/** Service for locked map. */
@Service
class LockedMapService(
    @Autowired private val lockedMapRepository: LockedMapRepository,
    @Autowired private val defaultCodeMapConfiguration: DefaultCodeMapConfiguration,
    @Autowired private val mapValidator: MapValidator,
) {

    fun getLockedMap(userId: UUID, mapType: GameMapTypeDto? = GameMapTypeDto.NORMAL): String {
        val defaultMap = HashMap<GameMapTypeDto, GameMap>()
        defaultMap[mapType ?: GameMapTypeDto.NORMAL] = defaultCodeMapConfiguration.defaultLockedGameMap
        return lockedMapRepository
            .findById(userId)
            .orElse(LockedMapEntity(userId = userId, lockedMap = defaultMap))
            .lockedMap[mapType]
            ?.map
            ?: defaultCodeMapConfiguration.defaultMap
    }

    fun updateLockedMap(userId: UUID, updateLatestMapRequestDto: UpdateLatestMapRequestDto) {
        mapValidator.validateMap(updateLatestMapRequestDto.map)
        val lockedMap = HashMap<GameMapTypeDto, GameMap>()
        lockedMap[updateLatestMapRequestDto.mapType ?: GameMapTypeDto.NORMAL] =
            GameMap(mapImage = updateLatestMapRequestDto.mapImage, map = updateLatestMapRequestDto.map)
        if (lockedMapRepository.findById(userId).isEmpty) {
            lockedMapRepository.save(
                LockedMapEntity(
                    userId = userId,
                    lockedMap = lockedMap,
                )
            )
        } else {
            val map = lockedMapRepository.findById(userId).get()
            map.lockedMap[updateLatestMapRequestDto.mapType ?: GameMapTypeDto.NORMAL] =
                GameMap(
                    mapImage = updateLatestMapRequestDto.mapImage, map = updateLatestMapRequestDto.map
                )
            val updatedMap = map.copy(lockedMap = map.lockedMap)
            lockedMapRepository.save(updatedMap)
        }
    }
}
