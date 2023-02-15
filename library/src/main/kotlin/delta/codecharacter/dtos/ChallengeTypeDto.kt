package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
* 
* Values: CODE,MAP
*/
enum class ChallengeTypeDto(val value: kotlin.String) {

    @JsonProperty("CODE") CODE("CODE"),
    @JsonProperty("MAP") MAP("MAP")
}

