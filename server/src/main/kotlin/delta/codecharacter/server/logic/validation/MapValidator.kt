package delta.codecharacter.server.logic.validation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import delta.codecharacter.server.exception.CustomException
import org.springframework.http.HttpStatus

class MapValidator {
    private val mapper = ObjectMapper().registerKotlinModule()
    private val invalidMapMessage = "Map is not valid"

    @Throws(CustomException::class)
    fun validateMap(mapJson: String) {
        val map: List<List<Int>>
        try {
            map = mapper.readValue(mapJson)
        } catch (_: Exception) {
            throw CustomException(HttpStatus.BAD_REQUEST, invalidMapMessage)
        }
        // TODO: Add checks for map size and spawn position limits
    }
}
