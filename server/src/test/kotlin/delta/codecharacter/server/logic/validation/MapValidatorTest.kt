package delta.codecharacter.server.logic.validation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import delta.codecharacter.server.exception.CustomException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class MapValidatorTest {

    private val mapper = ObjectMapper().registerKotlinModule()

    @Test
    fun `should accept valid JSON with 2D list`() {
        val testList = listOf(listOf(1, 2, 3), listOf(4, 5, 6))
        val json = mapper.writeValueAsString(testList)

        val mapValidator = MapValidator()

        assertDoesNotThrow { mapValidator.validateMap(json) }
    }

    @Test
    @Throws(CustomException::class)
    fun `should throw exception when JSON is not a 2D list`() {
        val testList = listOf(1, 2, 3)
        val json = mapper.writeValueAsString(testList)

        val mapValidator = MapValidator()

        val exception = assertThrows(CustomException::class.java) { mapValidator.validateMap(json) }
        assertThat(exception.message).isEqualTo("Map is not valid")
    }
}
