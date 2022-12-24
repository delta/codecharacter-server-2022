import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    id("org.springframework.boot") version "2.7.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.asciidoctor.convert") version "1.5.8"
    jacoco
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.allopen")
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

    implementation("org.springframework.boot:spring-boot-starter-amqp:3.0.0")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.0.0")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client:3.0.0")
    implementation("org.springframework.boot:spring-boot-starter-security:3.0.0")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.0.0")
    implementation("org.springframework.boot:spring-boot-starter-web:3.0.0")
    implementation("org.springframework.boot:spring-boot-starter-websocket:3.0.0")
    implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.21")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.21")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")
    implementation("com.sendgrid:sendgrid-java:4.8.3")
    implementation(project(":library"))
    implementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("com.ninja-squad:springmockk:4.0.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools:2.6.3")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:3.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.0.0") {
        exclude(module = "mockito-core")
    }
    testRuntimeOnly("de.flapdoodle.embed:de.flapdoodle.embed.mongo:4.3.3")
    testImplementation("org.springframework.amqp:spring-rabbit-test:2.4.7")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc:2.0.6.RELEASE")
    testImplementation("org.springframework.security:spring-security-test:5.5.1")
}

allOpen {
    annotation("org.springframework.data.mongodb.core.mapping.Document")
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

tasks.register("installGitHooks") {
    group = "build setup"
    description = "Install git hooks"
    doLast {
        val gitHooksDir = File(project.rootDir, ".git/hooks")
        val gitHookFile = File(gitHooksDir, "pre-commit")
        if (!gitHookFile.exists()) {
            val gitHook = File(project.rootDir, "scripts/pre-commit.sh")
            gitHook.copyTo(gitHookFile, true)
            gitHookFile.setExecutable(true)
        }
    }
}

tasks.named("build") {
    dependsOn("installGitHooks")
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
