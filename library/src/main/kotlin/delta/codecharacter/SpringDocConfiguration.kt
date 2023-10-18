package delta.codecharacter

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityScheme

@jakarta.annotation.Generated(value = ["org.openapitools.codegen.languages.KotlinSpringServerCodegen"])
@Configuration
class SpringDocConfiguration {

    @Bean
    fun apiInfo(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("CodeCharacter API")
                    .description("Specification of the CodeCharacter API")
                    .contact(
                        Contact()
                            .name("CodeCharacter Authors")
                            .url("https://delta.nitt.edu")
                            .email("delta@nitt.edu")
                    )
                    .license(
                        License()
                            .name("MIT")
                                                )
                    .version("2023.0.1")
            )
            .components(
                Components()
                    .addSecuritySchemes("http-bearer", SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                    )
                    .addSecuritySchemes("oauth2-github", SecurityScheme()
                        .type(SecurityScheme.Type.OAUTH2)
                    )
                    .addSecuritySchemes("oauth2-google", SecurityScheme()
                        .type(SecurityScheme.Type.OAUTH2)
                    )
            )
    }
}
