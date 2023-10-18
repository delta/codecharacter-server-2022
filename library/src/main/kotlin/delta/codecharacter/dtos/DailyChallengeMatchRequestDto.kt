package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import delta.codecharacter.dtos.LanguageDto
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Request Model for the daily challenge
 * @param &#x60;value&#x60; 
 * @param language 
 */
data class DailyChallengeMatchRequestDto(

    @Schema(example = "#include<iostream>", required = true, description = "")
    @get:JsonProperty("value", required = true) val `value`: kotlin.String,

    @field:Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("language") val language: LanguageDto? = null
) {

}

