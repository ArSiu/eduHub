plugins {
    id("org.springframework.boot") version "3.1.3" apply false
    id("io.spring.dependency-management") version "1.1.3" apply false
    id("com.google.protobuf") version "0.9.4" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.1" apply false
    id("io.freefair.lombok") version "8.1.0" apply false
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.spring") version "1.9.0" apply false
    kotlin("kapt") version "1.9.0" apply false
    kotlin("plugin.lombok") version "1.9.0" apply false
}

allprojects {
    group = "com.arsiu"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "com.google.protobuf")
}
