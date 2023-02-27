package delta.codecharacter.server.leaderboard

import com.fasterxml.jackson.databind.ObjectMapper
import delta.codecharacter.dtos.LeaderboardEntryDto
import delta.codecharacter.dtos.PublicUserDto
import delta.codecharacter.dtos.TierTypeDto
import delta.codecharacter.dtos.UserStatsDto
import delta.codecharacter.server.TestAttributes
import delta.codecharacter.server.WithMockCustomUser
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
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.math.BigDecimal

@AutoConfigureMockMvc
@SpringBootTest
internal class LeaderboardControllerIntegrationTest(@Autowired val mockMvc: MockMvc) {

    @Autowired private lateinit var jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder
    private lateinit var mapper: ObjectMapper

    @Autowired private lateinit var mongoTemplate: MongoTemplate

    @BeforeEach
    fun setUp() {
        mapper = jackson2ObjectMapperBuilder.build()
        mongoTemplate.save<PublicUserEntity>(TestAttributes.publicUser)
    }

    @Test
    @WithMockCustomUser
    fun `should get leaderboard when tier type is either TIER_PRACTICE , TIER1 and TIER2`() {
        val testUser = TestAttributes.publicUser.copy(tier = TierTypeDto.TIER1)
        mongoTemplate.save<PublicUserEntity>(testUser)
        val expectedDto =
            listOf(
                LeaderboardEntryDto(
                    user =
                    PublicUserDto(
                        username = testUser.username,
                        name = testUser.name,
                        tier = TierTypeDto.valueOf(testUser.tier.name),
                        country = testUser.country,
                        college = testUser.college,
                        avatarId = testUser.avatarId,
                    ),
                    stats =
                    UserStatsDto(
                        rating = BigDecimal(testUser.rating),
                        wins = testUser.wins,
                        losses = testUser.losses,
                        ties = testUser.ties
                    ),
                )
            )
        mockMvc
            .get("/leaderboard?page=0&size=2&tier=TIER1") { contentType = MediaType.APPLICATION_JSON }
            .andExpect {
                status { is2xxSuccessful() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(expectedDto)) }
            }
    }

    @Test
    @WithMockCustomUser
    fun `should get empty array for invalid tier type when leaderboard contains tier of different type`() {
        val testUser = TestAttributes.publicUser.copy(tier = TierTypeDto.TIER1)
        mongoTemplate.save<PublicUserEntity>(testUser)
        val expectedDto = listOf<LeaderboardEntryDto>()
        mockMvc
            .get("/leaderboard?page=0&size=2&tier=TIER2") { contentType = MediaType.APPLICATION_JSON }
            .andExpect {
                status { is2xxSuccessful() }
                content { contentType(MediaType.APPLICATION_JSON) }
                content { json(mapper.writeValueAsString(expectedDto)) }
            }
    }

    @Test
    @WithMockCustomUser
    fun `should return all entries when tier not found`() {
        val testUser = TestAttributes.publicUser
        val expectedDto =
            listOf(
                LeaderboardEntryDto(
                    user =
                    PublicUserDto(
                        username = testUser.username,
                        name = testUser.name,
                        tier = TierTypeDto.valueOf(testUser.tier.name),
                        country = testUser.country,
                        college = testUser.college,
                        avatarId = testUser.avatarId,
                    ),
                    stats =
                    UserStatsDto(
                        rating = BigDecimal(testUser.rating),
                        wins = testUser.wins,
                        losses = testUser.losses,
                        ties = testUser.ties
                    ),
                )
            )
        mockMvc.get("/leaderboard") { contentType = MediaType.APPLICATION_JSON }.andExpect {
            status { is2xxSuccessful() }
            content { contentType(MediaType.APPLICATION_JSON) }
            content { json(mapper.writeValueAsString(expectedDto)) }
        }
    }
    @AfterEach
    fun tearDown() {
        mongoTemplate.dropCollection<PublicUserEntity>()
    }
}
