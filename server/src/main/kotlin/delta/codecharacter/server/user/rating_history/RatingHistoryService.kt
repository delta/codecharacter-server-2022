package delta.codecharacter.server.user.rating_history

import delta.codecharacter.dtos.RatingHistoryDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Service
class RatingHistoryService(
    @Autowired private val ratingHistoryRepository: RatingHistoryRepository
) {
    fun create(userId: UUID) {
        val ratingHistory =
            RatingHistoryEntity(
                userId = userId, rating = 0.0, ratingDeviation = 0.0, validFrom = Instant.now()
            )
        ratingHistoryRepository.save(ratingHistory)
    }

    fun getRatingHistory(userId: UUID): List<RatingHistoryDto> {
        return ratingHistoryRepository.findAllByUserId(userId).map {
            RatingHistoryDto(
                rating = BigDecimal(it.rating),
                ratingDeviation = BigDecimal(it.ratingDeviation),
                validFrom = it.validFrom
            )
        }
    }
}
