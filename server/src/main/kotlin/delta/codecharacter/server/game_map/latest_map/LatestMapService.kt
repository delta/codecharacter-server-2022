package delta.codecharacter.server.game_map.latest_map

import delta.codecharacter.dtos.GameMapDto
import delta.codecharacter.dtos.GameMapTypeDto
import delta.codecharacter.dtos.UpdateLatestMapRequestDto
import delta.codecharacter.server.config.DefaultCodeMapConfiguration
import delta.codecharacter.server.game_map.GameMap
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

    fun getLatestMap(userId: UUID, mapType: GameMapTypeDto = GameMapTypeDto.NORMAL): GameMapDto {
        val defaultMap = HashMap<GameMapTypeDto, GameMap>()
        defaultMap[mapType] = defaultCodeMapConfiguration.defaultLatestGameMap
        return latestMapRepository
            .findById(userId)
            .orElse(
                LatestMapEntity(
                    userId = userId,
                    latestMap = defaultMap,
                )
            )
            .let { latestMap ->
                GameMapDto(
                    map = latestMap.latestMap[mapType]?.map ?: defaultCodeMapConfiguration.defaultMap,
                    mapImage = latestMap.latestMap[mapType]?.mapImage
                        ?: defaultCodeMapConfiguration.defaultMapImage,
                    lastSavedAt = latestMap.latestMap[mapType]?.lastSavedAt ?: Instant.MIN
                )
            }
    }

    fun updateLatestMap(userId: UUID, updateLatestMapDto: UpdateLatestMapRequestDto) {
        mapValidator.validateMap(updateLatestMapDto.map)
        val latestMap = HashMap<GameMapTypeDto, GameMap>()
        latestMap[updateLatestMapDto.mapType ?: GameMapTypeDto.NORMAL] =
            GameMap(
                mapImage = updateLatestMapDto.mapImage,
                map = updateLatestMapDto.map,
                lastSavedAt = Instant.now()
            )
        if (latestMapRepository.findById(userId).isEmpty) {
            latestMapRepository.save(
                LatestMapEntity(
                    userId = userId,
                    latestMap = latestMap,
                )
            )
        } else {
            val map = latestMapRepository.findById(userId).get()
            map.latestMap[updateLatestMapDto.mapType ?: GameMapTypeDto.NORMAL] =
                GameMap(
                    mapImage = updateLatestMapDto.mapImage,
                    map = updateLatestMapDto.map,
                    lastSavedAt = Instant.now()
                )
            val updatedMap = map.copy(latestMap = map.latestMap)
            latestMapRepository.save(updatedMap)
        }
    }
}
