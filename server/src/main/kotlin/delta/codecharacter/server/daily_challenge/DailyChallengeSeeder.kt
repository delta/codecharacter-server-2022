package delta.codecharacter.server.daily_challenge

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.io.File

@Component
class DailyChallengeSeeder {

    @Autowired private lateinit var dailyChallengeRepository: DailyChallengeRepository

    @Value(
        "/server/server/src/main/kotlin/delta/codecharacter/server/daily_challenge/dcConstants.json"
    )
    lateinit var filePath: String
    @EventListener(ApplicationReadyEvent::class)
    fun doSomethingAfterStartup() {
        println("Initialising daily challenges")

        //      Delete everything initially
        dailyChallengeRepository.deleteAll()

        //      Seed the database
        val jsonString = File(filePath).readText()
        val objectMapper = jacksonObjectMapper()
        val dcs: List<DailyChallengeEntity> = objectMapper.readValue(jsonString)

        dailyChallengeRepository.saveAll(dcs)
    }
}
