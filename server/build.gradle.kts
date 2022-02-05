import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    id("org.springframework.boot") version "2.6.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.asciidoctor.convert") version "1.5.8"
    jacoco
    kotlin("jvm")
    kotlin("plugin.spring")
    id("com.diffplug.spotless") version "6.2.1"
}

group = "delta.codecharacter"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val snippetsDir by extra { file("build/generated-snippets") }

dependencies {
    val mapstructVersion = "1.4.2.Final"

    implementation("org.springframework.boot:spring-boot-starter-amqp:2.6.3")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:2.6.3")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client:2.6.3")
    implementation("org.springframework.boot:spring-boot-starter-security:2.6.3")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.6.3")
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.3")
    implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0")
    implementation(project(":library"))
    implementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    compileOnly("org.mapstruct:mapstruct-processor:$mapstructVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
    testImplementation("com.ninja-squad:springmockk:3.1.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools:2.6.3")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:2.6.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.3") {
        exclude(module = "mockito-core")
    }
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:3.3.1")
    testImplementation("org.springframework.amqp:spring-rabbit-test:2.4.2")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc:2.0.6.RELEASE")
    testImplementation("org.springframework.security:spring-security-test:5.5.1")
    testAnnotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    outputs.dir(snippetsDir)
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("coverage"))
    }
}

tasks.asciidoctor {
    inputs.dir(snippetsDir)
    dependsOn(tasks.test)
}

configure<com.diffplug.gradle.spotless.SpotlessExtension> {
    kotlin {
        target("**/*.kt")
        ktfmt()
        ktlint()
    }
    kotlinGradle {
        ktlint()
    }
}
