package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Email
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import javax.validation.Valid
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

