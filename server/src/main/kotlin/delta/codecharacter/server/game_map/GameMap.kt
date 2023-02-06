package delta.codecharacter.server.game_map

import java.time.Instant

data class GameMap(val mapImage: String, val map: String, val lastSavedAt: Instant? = null)
