package delta.codecharacter.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableWebSecurity @SpringBootApplication class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}
