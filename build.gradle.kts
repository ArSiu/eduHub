import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Versions {
    const val mapstruct = "1.5.5.Final"
    const val springdoc = "2.1.0"
}

plugins {
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.spring") version "1.9.0"
    kotlin("kapt") version "1.9.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.1.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.mapstruct:mapstruct:${Versions.mapstruct}")
    kapt("org.mapstruct:mapstruct-processor:${Versions.mapstruct}")
    api("org.springdoc:springdoc-openapi-starter-webmvc-ui:${Versions.springdoc}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

group = "com.arsiu"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
