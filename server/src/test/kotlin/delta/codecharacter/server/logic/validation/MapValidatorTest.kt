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
    fun `should accept valid JSON with 2D list of dimension 64x64`() {
        val testList = List(64) { List(64) { 0 } }
        val json = mapper.writeValueAsString(testList)

        val mapValidator = MapValidator()

        assertDoesNotThrow { mapValidator.validateMap(json) }
    }

    @Test
    @Throws(CustomException::class)
    fun `should throw exception with 2D list without dimension 64x64`() {
        val testList = List(64) { List(63) { 0 } }
        val json = mapper.writeValueAsString(testList)

        val mapValidator = MapValidator()

        val exception = assertThrows(CustomException::class.java) { mapValidator.validateMap(json) }
        assertThat(exception.message).isEqualTo("Map is not valid")
    }

    @Test
    @Throws(CustomException::class)
    fun `should throw exception when JSON is not a 2D list`() {
        val testList = listOf(0..63)
        val json = mapper.writeValueAsString(testList)

        val mapValidator = MapValidator()

        val exception = assertThrows(CustomException::class.java) { mapValidator.validateMap(json) }
        assertThat(exception.message).isEqualTo("Map is not valid")
    }

    @Test
    @Throws(CustomException::class)
    fun `should throw exception with 2D list with defenders in attacker spawn positions`() {
        val testList = MutableList(64) { MutableList(64) { 0 } }
        testList[0][0] = 1

        val json = mapper.writeValueAsString(testList)

        val mapValidator = MapValidator()

        val exception = assertThrows(CustomException::class.java) { mapValidator.validateMap(json) }
        assertThat(exception.message).isEqualTo("Map is not valid")
    }

    @Test
    @Throws(CustomException::class)
    fun `should throw exception with 2D list with invalid defender ID`() {
        val testList = MutableList(64) { MutableList(64) { 0 } }
        testList[5][5] = -1

        val json = mapper.writeValueAsString(testList)

        val mapValidator = MapValidator()

        val exception = assertThrows(CustomException::class.java) { mapValidator.validateMap(json) }
        assertThat(exception.message).isEqualTo("Map is not valid")
    }
}
