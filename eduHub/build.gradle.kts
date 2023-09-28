import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Versions {
    const val mapstruct = "1.5.5.Final"
    const val springdoc = "2.1.0"
    const val lombok = "1.18.28"
    const val protobuf = "3.24.2"
    const val testcontainers = "1.19.0"
    const val jupiter = "5.8.1"
    const val jnats = "2.16.14"
}

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("io.gitlab.arturbosch.detekt")
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.spring")
    kotlin("plugin.lombok")
    id("io.freefair.lombok")
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-tomcat")
    implementation("org.springframework.boot:spring-boot-starter-webflux") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-reactor-netty")
    }
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    // Kotlin & Protobuf
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.google.protobuf:protobuf-java:${Versions.protobuf}")

    // MapStruct & Lombok
    implementation("org.mapstruct:mapstruct:${Versions.mapstruct}")
    kapt("org.mapstruct:mapstruct-processor:${Versions.mapstruct}")
    implementation("org.projectlombok:lombok:${Versions.lombok}")
    kapt("org.projectlombok:lombok:${Versions.lombok}")

    // Nats
    implementation(project(":natsSubjects"))
    implementation("io.nats:jnats:${Versions.jnats}")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers:${Versions.testcontainers}")
    testImplementation("org.testcontainers:junit-jupiter:${Versions.testcontainers}")
    testImplementation("org.testcontainers:mongodb:${Versions.testcontainers}")
    testImplementation("org.junit.jupiter:junit-jupiter:${Versions.jupiter}")

    //other
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${Versions.springdoc}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("spring.profiles.active", "test")
}
