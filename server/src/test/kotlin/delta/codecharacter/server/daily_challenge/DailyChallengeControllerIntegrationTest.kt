package delta.codecharacter.server.daily_challenge

import com.fasterxml.jackson.databind.ObjectMapper
import delta.codecharacter.dtos.DailyChallengeMatchRequestDto
import delta.codecharacter.server.TestAttributes
import delta.codecharacter.server.WithMockCustomUser
import delta.codecharacter.server.user.UserEntity
import delta.codecharacter.server.user.public_user.PublicUserEntity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.dropCollection
import org.springframework.http.MediaType
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.Instant

@AutoConfigureMockMvc
@SpringBootTest
internal class DailyChallengeControllerIntegrationTest(@Autowired val mockMvc: MockMvc) {

    @Autowired private lateinit var dailyChallengeService: DailyChallengeService

    @Autowired private lateinit var jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder
    private lateinit var mapper: ObjectMapper

    @Autowired private lateinit var mongoTemplate: MongoTemplate

    @BeforeEach
    fun setUp() {
        mapper = jackson2ObjectMapperBuilder.build()
        mongoTemplate.save<UserEntity>(TestAttributes.user)
        mongoTemplate.save<PublicUserEntity>(TestAttributes.publicUser)
        ReflectionTestUtils.setField(dailyChallengeService, "startDate", Instant.now().toString())
    }

    @Test
    @WithMockCustomUser
    fun `should not allow daily challenge match submission if already completed`() {
        mockMvc
            .post("/dc/submit") {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(DailyChallengeMatchRequestDto(value = "[[0,0,0]]"))
            }
            .andExpect {
                status { isBadRequest() }
                content {
                    mapper.writeValueAsString(
                        mapOf("message" to "You have already completed your daily challenge")
                    )
                }
            }
    }

    @AfterEach
    fun tearDown() {
        mongoTemplate.dropCollection<UserEntity>()
        mongoTemplate.dropCollection<PublicUserEntity>()
    }
}
