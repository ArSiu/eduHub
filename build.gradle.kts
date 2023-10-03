object Versions {
    const val grpc = "1.58.0"
    const val rgrpc = "1.2.4"
    const val reactor = "3.5.10"
    const val protobufjava = "3.24.2"
}

plugins {
    id("org.springframework.boot") version "3.1.3" apply false
    id("io.spring.dependency-management") version "1.1.3" apply false
    id("com.google.protobuf") version "0.9.4"
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
        maven { url = uri("https://packages.confluent.io/maven/") }
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "com.google.protobuf")

    dependencies {
        //Reactor
        implementation("io.projectreactor:reactor-core:${Versions.reactor}")

        //gRPC
        implementation("io.grpc:grpc-core:${Versions.grpc}")
        implementation("io.grpc:grpc-census:${Versions.grpc}")
        implementation("io.grpc:grpc-protobuf:${Versions.grpc}")
        implementation("io.grpc:grpc-netty:${Versions.grpc}")
        implementation("io.grpc:grpc-stub:${Versions.grpc}")

        //Reactive-gRPC
        implementation("com.salesforce.servicelibs:reactor-grpc:${Versions.rgrpc}")
        implementation("com.salesforce.servicelibs:reactive-grpc-common:${Versions.rgrpc}")
        implementation("com.salesforce.servicelibs:reactor-grpc-stub:${Versions.rgrpc}")

        //Proto
        implementation("com.google.protobuf:protobuf-java:${Versions.protobufjava}")
        implementation("com.google.protobuf:protobuf-java-util:${Versions.protobufjava}")

        //Test
        testImplementation ("io.grpc:grpc-testing:${Versions.grpc}")
        testImplementation("io.projectreactor:reactor-test:${Versions.reactor}")
    }
}
