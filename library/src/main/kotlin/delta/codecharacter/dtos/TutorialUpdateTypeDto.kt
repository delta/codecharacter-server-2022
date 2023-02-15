package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
* 
* Values: NEXT,PREVIOUS,SKIP
*/
enum class TutorialUpdateTypeDto(val value: kotlin.String) {

    @JsonProperty("NEXT") NEXT("NEXT"),
    @JsonProperty("PREVIOUS") PREVIOUS("PREVIOUS"),
    @JsonProperty("SKIP") SKIP("SKIP")
}

