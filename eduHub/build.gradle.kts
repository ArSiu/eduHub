import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Versions {
    const val mapstruct = "1.5.5.Final"
    const val springdoc = "2.1.0"
    const val lombok = "1.18.28"
    const val testcontainers = "1.18.0"
    const val jupiter = "5.8.1"
    const val jnats = "2.16.14"
    const val springKafa = "3.0.11"
    const val reactorKafka = "1.3.21"
}

plugins {
    application
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
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("net.devh:grpc-spring-boot-starter:2.15.0.RELEASE")
    implementation("net.devh:grpc-server-spring-boot-starter:2.15.0.RELEASE")

    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    // MapStruct & Lombok
    implementation("org.mapstruct:mapstruct:${Versions.mapstruct}")
    kapt("org.mapstruct:mapstruct-processor:${Versions.mapstruct}")
    implementation("org.projectlombok:lombok:${Versions.lombok}")
    kapt("org.projectlombok:lombok:${Versions.lombok}")

    //proto
    implementation(project(":proto"))
    implementation(project(":eduHub:common"))
    implementation(project(":eduHub:assignment"))
    implementation(project(":eduHub:lesson"))
    implementation(project(":eduHub:chapter"))
    implementation(project(":eduHub:course"))
    implementation(project(":eduHub:user"))

    // Nats
    implementation("io.nats:jnats:${Versions.jnats}")

    //Kafka
    implementation("org.springframework.kafka:spring-kafka:${Versions.springKafa}")
    implementation("io.projectreactor.kafka:reactor-kafka:${Versions.reactorKafka}")
    implementation("io.confluent:kafka-protobuf-serializer:7.4.0")


    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers:${Versions.testcontainers}")
    testImplementation("org.testcontainers:junit-jupiter:${Versions.testcontainers}")
    testImplementation("org.testcontainers:mongodb:${Versions.testcontainers}")
    testImplementation("org.junit.jupiter:junit-jupiter:${Versions.jupiter}")

    //other
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:${Versions.springdoc}")
}

allprojects {
    group = "com.arsiu"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
        maven { url = uri("https://packages.confluent.io/maven/") }
    }
}

subprojects {

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "io.freefair.lombok")
    apply(plugin = "com.google.protobuf")

    dependencies {
        // Module
        implementation(project(":"))

        // Spring Boot
        implementation("org.springframework.boot:spring-boot-starter-webflux")
        implementation("org.springframework.boot:spring-boot-starter-validation")
        implementation("org.springframework.boot:spring-boot-starter-mail")
        implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
        implementation("net.devh:grpc-spring-boot-starter:2.15.0.RELEASE")
        implementation("net.devh:grpc-server-spring-boot-starter:2.15.0.RELEASE")

        // Kotlin
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib")

        // MapStruct & Lombok
        implementation("org.mapstruct:mapstruct:${Versions.mapstruct}")
        kapt("org.mapstruct:mapstruct-processor:${Versions.mapstruct}")
        implementation("org.projectlombok:lombok:${Versions.lombok}")
        kapt("org.projectlombok:lombok:${Versions.lombok}")

        //proto
        implementation(project(":proto"))

        // Nats
        implementation("io.nats:jnats:${Versions.jnats}")

        //Kafka
        implementation("org.springframework.kafka:spring-kafka:${Versions.springKafa}")
        implementation("io.projectreactor.kafka:reactor-kafka:${Versions.reactorKafka}")
        implementation("io.confluent:kafka-protobuf-serializer:7.4.0")

        // Testing
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.testcontainers:testcontainers:${Versions.testcontainers}")
        testImplementation("org.testcontainers:junit-jupiter:${Versions.testcontainers}")
        testImplementation("org.testcontainers:mongodb:${Versions.testcontainers}")
        testImplementation("org.junit.jupiter:junit-jupiter:${Versions.jupiter}")

        //other
        implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:${Versions.springdoc}")
    }

    // Task configurations
    tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        enabled = false
    }

    tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
        enabled = false
    }
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
