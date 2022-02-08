package delta.codecharacter.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@SpringBootApplication
class ServerApplication {
    @Bean
    fun embeddedMongoAutoConfiguration(
        mongoProperties: MongoProperties?
    ): EmbeddedMongoAutoConfiguration? {
        return EmbeddedMongoAutoConfiguration(mongoProperties)
    }
}

fun main(args: Array<String>) {
    System.setProperty("os.arch", "x86_64")
    runApplication<ServerApplication>(*args)
}
