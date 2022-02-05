package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * Values: IDLE,EXECUTING,EXECUTED,EXECUTE_ERROR
 */
enum class GameStatusDto(val value: String) {

    @JsonProperty("IDLE")
    IDLE("IDLE"),

    @JsonProperty("EXECUTING")
    EXECUTING("EXECUTING"),

    @JsonProperty("EXECUTED")
    EXECUTED("EXECUTED"),

    @JsonProperty("EXECUTE_ERROR")
    EXECUTE_ERROR("EXECUTE_ERROR");
}
