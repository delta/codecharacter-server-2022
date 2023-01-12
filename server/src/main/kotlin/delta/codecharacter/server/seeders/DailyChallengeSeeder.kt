package delta.codecharacter.server.seeders

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import delta.codecharacter.server.daily_challenge.DailyChallengeEntity
import delta.codecharacter.server.daily_challenge.DailyChallengeRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class DailyChallengeSeeder {

    @Autowired private lateinit var dailyChallengeRepository: DailyChallengeRepository

    private val logger: Logger = LoggerFactory.getLogger(DailyChallengeSeeder::class.java)
    @EventListener(ApplicationReadyEvent::class)
    fun doSomethingAfterStartup() {

        if (dailyChallengeRepository.findAll().isEmpty()) {
            logger.info("Seeding daily_challenges")

            val jsonString = this::class.java.classLoader.getResource("dcConstants.json")?.readText()
            if (!jsonString.isNullOrEmpty()) {
                val objectMapper = jacksonObjectMapper()
                val dcs: List<DailyChallengeObject> = objectMapper.readValue(jsonString)
                var dcEntities: List<DailyChallengeEntity> = listOf()
                dcs.forEach {
                    val id = UUID.randomUUID()
                    dcEntities =
                        dcEntities.plus(
                            DailyChallengeEntity(
                                id = id,
                                day = it.day,
                                chall = it.chall,
                                challName = it.challName,
                                challType = it.challType,
                                description = it.description
                            )
                        )
                }
                dailyChallengeRepository.saveAll(dcEntities)
            } else {
                logger.error("dcConstants.json is empty or doesn't exist")
            }
        }
        else {
            logger.info("Daily Challenges seeded already")
        }
    }
}
