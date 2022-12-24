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
* Language of source files
* Values: C,CPP,JAVA,PYTHON
*/
enum class LanguageDto(val value: kotlin.String) {

    @JsonProperty("C") C("C"),
    @JsonProperty("CPP") CPP("CPP"),
    @JsonProperty("JAVA") JAVA("JAVA"),
    @JsonProperty("PYTHON") PYTHON("PYTHON")
}

