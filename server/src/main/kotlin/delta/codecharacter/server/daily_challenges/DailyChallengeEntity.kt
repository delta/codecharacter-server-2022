package delta.codecharacter.server.daily_challenges

import delta.codecharacter.dtos.ChallengeTypeDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "daily_challenges") // change it later
data class DailyChallengeEntity(
    @Id val id: UUID,
    val challName: String,
    val challType: ChallengeTypeDto,
    val chall: String,
    )
