import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Versions {
    const val mapstruct = "1.5.5.Final"
    const val springdoc = "2.1.0"
    const val lombok = "1.18.28"
}

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.google.protobuf")
    id("io.gitlab.arturbosch.detekt")
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.spring")
    kotlin("plugin.lombok") version "1.9.10"
    id("io.freefair.lombok") version "8.1.0"
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

    implementation(project(":natsSubjects"))
    implementation("io.nats:jnats:2.16.14")
    implementation("com.google.protobuf:protobuf-java:3.24.2")

    implementation("org.mapstruct:mapstruct:${Versions.mapstruct}")
    kapt("org.mapstruct:mapstruct-processor:${Versions.mapstruct}")
    implementation("org.projectlombok:lombok:${Versions.lombok}")
    kapt("org.projectlombok:lombok:${Versions.lombok}")

    api("org.springdoc:springdoc-openapi-starter-webmvc-ui:${Versions.springdoc}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("org.testcontainers:testcontainers:1.19.0")
    testImplementation("org.testcontainers:junit-jupiter:1.19.0")
    testImplementation("org.testcontainers:mongodb:1.19.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
