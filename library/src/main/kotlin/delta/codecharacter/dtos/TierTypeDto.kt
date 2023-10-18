package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
* 
* Values: TIER_PRACTICE,TIER1,TIER2,TIER3,TIER4
*/
enum class TierTypeDto(val value: kotlin.String) {

    @JsonProperty("TIER_PRACTICE") TIER_PRACTICE("TIER_PRACTICE"),
    @JsonProperty("TIER1") TIER1("TIER1"),
    @JsonProperty("TIER2") TIER2("TIER2"),
    @JsonProperty("TIER3") TIER3("TIER3"),
    @JsonProperty("TIER4") TIER4("TIER4")
}

