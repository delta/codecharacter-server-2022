package delta.codecharacter.server.user.rating_history

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.UUID

@Document(collection = "rating_history")
data class RatingHistoryEntity(
    @Id val userId: UUID,
    val rating: Double,
    val ratingDeviation: Double,
    val validFrom: Instant
)
