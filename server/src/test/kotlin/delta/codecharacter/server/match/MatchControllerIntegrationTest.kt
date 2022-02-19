package delta.codecharacter.server.match

import com.fasterxml.jackson.databind.ObjectMapper
import delta.codecharacter.dtos.CreateMatchRequestDto
import delta.codecharacter.dtos.MatchModeDto
import delta.codecharacter.server.WithMockCustomUser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.util.UUID

@AutoConfigureMockMvc
@SpringBootTest
internal class MatchControllerIntegrationTest(@Autowired val mockMvc: MockMvc) {

    @Autowired private lateinit var jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        mapper = jackson2ObjectMapperBuilder.build()
    }

    @Test
    @WithMockCustomUser
    fun `should return bad request when revision IDs don't belong to the user for self match`() {
        val createMatchRequestDto =
            CreateMatchRequestDto(
                mode = MatchModeDto.SELF,
                codeRevisionId = UUID.randomUUID(),
                mapRevisionId = UUID.randomUUID()
            )

        mockMvc
            .post("/user/matches") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(createMatchRequestDto)
            }
            .andExpect {
                status { isBadRequest() }
                content { mapper.writeValueAsString(mapOf("message" to "Invalid revision ID")) }
            }
    }
}
