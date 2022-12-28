package delta.codecharacter.dtos

import java.util.Objects
import com.fasterxml.jackson.annotation.JsonValue
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
* Language of source files
* Values: C,CPP,JAVA,PYTHON
*/
enum class LanguageDto(val value: kotlin.String) {

    @JsonProperty("C") C("C"),
    @JsonProperty("CPP") CPP("CPP"),
    @JsonProperty("JAVA") JAVA("JAVA"),
    @JsonProperty("PYTHON") PYTHON("PYTHON")
}

