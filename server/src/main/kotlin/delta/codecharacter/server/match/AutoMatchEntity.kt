package delta.codecharacter.server.match

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document(collection = "auto_match")
data class AutoMatchEntity(@Id val matchId: UUID, val tries: Int)
