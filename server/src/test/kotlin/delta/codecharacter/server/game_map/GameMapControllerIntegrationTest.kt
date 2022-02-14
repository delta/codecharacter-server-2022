package delta.codecharacter.server.game_map

import com.fasterxml.jackson.databind.ObjectMapper
import delta.codecharacter.dtos.CreateMapRevisionRequestDto
import delta.codecharacter.dtos.GameMapDto
import delta.codecharacter.dtos.GameMapRevisionDto
import delta.codecharacter.dtos.UpdateLatestMapRequestDto
import delta.codecharacter.server.TestAttributes
import delta.codecharacter.server.WithMockCustomUser
import delta.codecharacter.server.game_map.latest_map.LatestMapEntity
import delta.codecharacter.server.game_map.locked_map.LockedMapEntity
import delta.codecharacter.server.game_map.map_revision.MapRevisionEntity
import delta.codecharacter.server.user.UserEntity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.dropCollection
import org.springframework.data.mongodb.core.findAll
import org.springframework.http.MediaType
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@AutoConfigureMockMvc
@SpringBootTest
internal class GameMapControllerIntegrationTest(@Autowired val mockMvc: MockMvc) {
    @Autowired private lateinit var mongoTemplate: MongoTemplate

    @Autowired private lateinit var jackson2ObjectMapperBuilder: Jackson2ObjectMapperBuilder
    private lateinit var mapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        mapper = jackson2ObjectMapperBuilder.build()
        mongoTemplate.save<UserEntity>(TestAttributes.user)
    }

    @Test
    @WithMockCustomUser
    fun `should create map revision`() {

        val validMap = mapper.writeValueAsString(List(64) { List(64) { 0 } })
        val dto = CreateMapRevisionRequestDto(map = validMap, message = "message")

        mockMvc
            .post("/user/map/revisions") {
                content = mapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
            }
            .andExpect { status { is2xxSuccessful() } }

        val mapRevisions = mongoTemplate.findAll<MapRevisionEntity>()
        assert(mapRevisions.size == 1)

        val mapRevision = mapRevisions.first()
        assert(mapRevision.map == dto.map)
        assert(mapRevision.userId == TestAttributes.user.id)
    }

    @Test
    @WithMockCustomUser
    fun `should get map revisions`() {
        val mapRevisionEntity =
            MapRevisionEntity(
                id = UUID.randomUUID(),
                map = "map",
                message = "message",
                userId = TestAttributes.user.id,
                parentRevision = null,
                createdAt = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            )
        mongoTemplate.insert<MapRevisionEntity>(mapRevisionEntity)

        val expectedDto =
            listOf(
                GameMapRevisionDto(
                    id = mapRevisionEntity.id,
                    map = mapRevisionEntity.map,
                    message = "message",
                    createdAt = mapRevisionEntity.createdAt
                )
            )

        mockMvc.get("/user/map/revisions") { contentType = MediaType.APPLICATION_JSON }.andExpect {
            status { is2xxSuccessful() }
            content { json(mapper.writeValueAsString(expectedDto)) }
        }
    }

    @Test
    @WithMockCustomUser
    fun `should get latest map`() {
        val latestMapEntity =
            LatestMapEntity(
                userId = TestAttributes.user.id,
                map = "map",
                lastSavedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            )
        mongoTemplate.insert<LatestMapEntity>(latestMapEntity)

        val expectedDto =
            GameMapDto(map = latestMapEntity.map, lastSavedAt = latestMapEntity.lastSavedAt)

        mockMvc.get("/user/map/latest") { contentType = MediaType.APPLICATION_JSON }.andExpect {
            status { is2xxSuccessful() }
            content { json(mapper.writeValueAsString(expectedDto)) }
        }
    }

    @Test
    @WithMockCustomUser
    fun `should update latest map`() {
        val validMap = mapper.writeValueAsString(List(64) { List(64) { 0 } })
        val oldMapEntity =
            LatestMapEntity(
                userId = TestAttributes.user.id,
                map = validMap,
                lastSavedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            )

        mongoTemplate.insert<LatestMapEntity>(oldMapEntity)

        val dto = UpdateLatestMapRequestDto(map = validMap)

        mockMvc
            .post("/user/map/latest") {
                content = mapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
            }
            .andExpect { status { is2xxSuccessful() } }

        val latestMap = mongoTemplate.findAll<LatestMapEntity>().first()
        assert(latestMap.map == dto.map)
        assert(latestMap.lastSavedAt != oldMapEntity.lastSavedAt)
    }

    @Test
    @WithMockCustomUser
    fun `should update latest map with lock`() {
        val validMap = mapper.writeValueAsString(List(64) { List(64) { 0 } })
        val oldMapEntity =
            LatestMapEntity(
                userId = TestAttributes.user.id,
                map = validMap,
                lastSavedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            )
        mongoTemplate.insert<LatestMapEntity>(oldMapEntity)

        val dto = UpdateLatestMapRequestDto(map = validMap, lock = true)

        mockMvc
            .post("/user/map/latest") {
                content = mapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
            }
            .andExpect { status { is2xxSuccessful() } }

        val latestMap = mongoTemplate.findAll<LatestMapEntity>().first()
        assert(latestMap.map == dto.map)
        assert(latestMap.lastSavedAt != oldMapEntity.lastSavedAt)

        val lockedMap = mongoTemplate.findAll<LockedMapEntity>().first()
        assert(lockedMap.map == dto.map)
    }

    @AfterEach
    fun tearDown() {
        mongoTemplate.dropCollection<MapRevisionEntity>()
        mongoTemplate.dropCollection<LatestMapEntity>()
        mongoTemplate.dropCollection<LockedMapEntity>()
        mongoTemplate.dropCollection<UserEntity>()
    }
}
