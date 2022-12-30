import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "delta.codecharacter"
version = "2023.0.1"

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

plugins {
    id("org.springframework.boot") version "3.0.0" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm")
    kotlin("plugin.jpa")
    kotlin("plugin.spring")
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.0.0")
    implementation("org.springframework.boot:spring-boot-starter-web:3.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.14.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.0")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.7")
    implementation("io.swagger.core.v3:swagger-core:2.2.7")
    implementation("org.hibernate:hibernate-core:6.1.6.Final")
    implementation("mysql:mysql-connector-java:8.0.30")
    implementation("org.apache.commons:commons-dbcp2:2.9.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.6.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.0.0") {
        exclude(module = "junit")
    }
}
