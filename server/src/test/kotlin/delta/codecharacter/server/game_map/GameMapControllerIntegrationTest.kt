package delta.codecharacter.server.game_map

import com.fasterxml.jackson.databind.ObjectMapper
import delta.codecharacter.dtos.CreateMapRevisionRequestDto
import delta.codecharacter.dtos.GameMapDto
import delta.codecharacter.dtos.GameMapRevisionDto
import delta.codecharacter.dtos.GameMapTypeDto
import delta.codecharacter.dtos.UpdateLatestMapRequestDto
import delta.codecharacter.server.TestAttributes
import delta.codecharacter.server.WithMockCustomUser
import delta.codecharacter.server.game_map.latest_map.LatestMapEntity
import delta.codecharacter.server.game_map.locked_map.LockedMapEntity
import delta.codecharacter.server.game_map.map_revision.MapRevisionEntity
import delta.codecharacter.server.user.UserEntity
import org.assertj.core.api.Assertions.assertThat
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
        val dto =
            CreateMapRevisionRequestDto(
                map = validMap, message = "message", mapImage = "", mapType = GameMapTypeDto.NORMAL
            )

        mockMvc
            .post("/user/map/revisions") {
                content = mapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
            }
            .andExpect { status { is2xxSuccessful() } }

        val mapRevisions = mongoTemplate.findAll<MapRevisionEntity>()
        assertThat(mapRevisions.size).isEqualTo(1)

        val mapRevision = mapRevisions.first()
        assertThat(mapRevision.map).isEqualTo(dto.map)
        assertThat(mapRevision.userId).isEqualTo(TestAttributes.user.id)
    }

    @Test
    @WithMockCustomUser
    fun `should get map revisions`() {
        val mapRevisionEntity =
            MapRevisionEntity(
                id = UUID.randomUUID(),
                map = "map",
                message = "message",
                mapImage = "",
                mapType = GameMapTypeDto.NORMAL,
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
        val latestMap = HashMap<GameMapTypeDto, GameMap>()
        latestMap[GameMapTypeDto.NORMAL] =
            GameMap(
                map = "map",
                mapImage = "base64",
                lastSavedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            )
        val latestMapEntity = LatestMapEntity(userId = TestAttributes.user.id, latestMap = latestMap)
        mongoTemplate.insert<LatestMapEntity>(latestMapEntity)

        val expectedDto =
            GameMapDto(
                map = latestMapEntity.latestMap[GameMapTypeDto.NORMAL]?.map.toString(),
                lastSavedAt = latestMapEntity.latestMap[GameMapTypeDto.NORMAL]?.lastSavedAt
                    ?: Instant.MIN,
                mapImage = "base64"
            )

        mockMvc.get("/user/map/latest") { contentType = MediaType.APPLICATION_JSON }.andExpect {
            status { is2xxSuccessful() }
            content { json(mapper.writeValueAsString(expectedDto)) }
        }
    }

    @Test
    @WithMockCustomUser
    fun `should update latest map`() {
        val oldMap = HashMap<GameMapTypeDto, GameMap>()
        oldMap[GameMapTypeDto.NORMAL] =
            GameMap(
                map = "map",
                mapImage = "base64",
                lastSavedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            )
        val validMap = mapper.writeValueAsString(List(64) { List(64) { 0 } })
        val oldMapEntity = LatestMapEntity(userId = TestAttributes.user.id, latestMap = oldMap)

        mongoTemplate.insert<LatestMapEntity>(oldMapEntity)

        val dto =
            UpdateLatestMapRequestDto(map = validMap, mapImage = "", mapType = GameMapTypeDto.NORMAL)

        mockMvc
            .post("/user/map/latest") {
                content = mapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
            }
            .andExpect { status { is2xxSuccessful() } }

        val latestMap = mongoTemplate.findAll<LatestMapEntity>().first()
        assertThat(latestMap.latestMap[GameMapTypeDto.NORMAL]?.map).isEqualTo(dto.map)
        assertThat(latestMap.latestMap[GameMapTypeDto.NORMAL]?.lastSavedAt)
            .isNotEqualTo(oldMapEntity.latestMap[GameMapTypeDto.NORMAL]?.lastSavedAt)
    }

    @Test
    @WithMockCustomUser
    fun `should update latest map with lock`() {
        val validMap = mapper.writeValueAsString(List(64) { List(64) { 0 } })
        val oldMap = HashMap<GameMapTypeDto, GameMap>()
        oldMap[GameMapTypeDto.NORMAL] =
            GameMap(
                map = "map",
                mapImage = "base64",
                lastSavedAt = Instant.now().truncatedTo(ChronoUnit.MILLIS)
            )
        val oldMapEntity = LatestMapEntity(userId = TestAttributes.user.id, latestMap = oldMap)
        mongoTemplate.insert<LatestMapEntity>(oldMapEntity)

        val dto =
            UpdateLatestMapRequestDto(
                map = validMap, lock = true, mapImage = "", mapType = GameMapTypeDto.NORMAL
            )

        mockMvc
            .post("/user/map/latest") {
                content = mapper.writeValueAsString(dto)
                contentType = MediaType.APPLICATION_JSON
            }
            .andExpect { status { is2xxSuccessful() } }

        val latestMap = mongoTemplate.findAll<LatestMapEntity>().first()
        assertThat(latestMap.latestMap[GameMapTypeDto.NORMAL]?.map).isEqualTo(dto.map)
        assertThat(latestMap.latestMap[GameMapTypeDto.NORMAL]?.map)
            .isNotEqualTo(oldMapEntity.latestMap[GameMapTypeDto.NORMAL]?.map)

        val lockedMap = mongoTemplate.findAll<LockedMapEntity>().first()
        assertThat(lockedMap.lockedMap[GameMapTypeDto.NORMAL]?.map).isEqualTo(dto.map)
    }

    @AfterEach
    fun tearDown() {
        mongoTemplate.dropCollection<MapRevisionEntity>()
        mongoTemplate.dropCollection<LatestMapEntity>()
        mongoTemplate.dropCollection<LockedMapEntity>()
        mongoTemplate.dropCollection<UserEntity>()
    }
}
