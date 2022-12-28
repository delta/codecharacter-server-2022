package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Notification model
 * @param id
 * @param title
 * @param content
 * @param createdAt
 * @param read
 */
data class NotificationDto(

    @Schema(example = "123e4567-e89b-12d3-a456-426614174000", required = true, description = "")
    @field:JsonProperty("id", required = true) val id: java.util.UUID,

    @Schema(example = "Test notification", required = true, description = "")
    @field:JsonProperty("title", required = true) val title: kotlin.String,

    @Schema(example = "Test", required = true, description = "")
    @field:JsonProperty("content", required = true) val content: kotlin.String,

    @Schema(example = "2021-01-01T00:00Z", required = true, description = "")
    @field:JsonProperty("createdAt", required = true) val createdAt: java.time.Instant,

    @Schema(example = "null", required = true, description = "")
    @field:JsonProperty("read", required = true) val read: kotlin.Boolean = false
) {

}

