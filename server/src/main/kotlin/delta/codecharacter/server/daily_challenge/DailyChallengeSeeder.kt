package delta.codecharacter.server.daily_challenge

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class DailyChallengeSeeder {

    @Autowired private lateinit var dailyChallengeRepository: DailyChallengeRepository

    private val logger: Logger = LoggerFactory.getLogger(DailyChallengeSeeder::class.java)
    @EventListener(ApplicationReadyEvent::class)
    fun doSomethingAfterStartup() {

        if (dailyChallengeRepository.findAll().isEmpty()) {
            logger.info("Seeding daily_challenges")

            dailyChallengeRepository.deleteAll()

            val jsonString = this::class.java.classLoader.getResource("dcConstants.json").readText()
            if (jsonString.isNotEmpty()) {
                val objectMapper = jacksonObjectMapper()
                val dcs: List<DailyChallengeEntity> = objectMapper.readValue(jsonString)
                dailyChallengeRepository.saveAll(dcs)
            } else {
                logger.error("dcConstants.json not found or doesn't exist")
            }
        }
    }
}
