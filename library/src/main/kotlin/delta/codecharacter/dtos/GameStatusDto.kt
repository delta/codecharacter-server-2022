package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.*
import jakarta.validation.Valid
import io.swagger.v3.oas.annotations.media.Schema

/**
* 
* Values: IDLE,EXECUTING,EXECUTED,EXECUTE_ERROR
*/
enum class GameStatusDto(val value: kotlin.String) {

    @JsonProperty("IDLE") IDLE("IDLE"),
    @JsonProperty("EXECUTING") EXECUTING("EXECUTING"),
    @JsonProperty("EXECUTED") EXECUTED("EXECUTED"),
    @JsonProperty("EXECUTE_ERROR") EXECUTE_ERROR("EXECUTE_ERROR")
}

