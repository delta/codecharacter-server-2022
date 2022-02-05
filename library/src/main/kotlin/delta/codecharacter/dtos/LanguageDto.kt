package delta.codecharacter.dtos

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Language of source files
 * Values: C,CPP,JAVA,PYTHON
 */
enum class LanguageDto(val value: String) {

    @JsonProperty("C")
    C("C"),

    @JsonProperty("CPP")
    CPP("CPP"),

    @JsonProperty("JAVA")
    JAVA("JAVA"),

    @JsonProperty("PYTHON")
    PYTHON("PYTHON");
}
