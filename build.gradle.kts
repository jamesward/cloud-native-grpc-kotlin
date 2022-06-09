plugins {
    id("com.google.protobuf") version "0.8.18" apply false
    kotlin("jvm") version "1.6.21" apply false
    kotlin("multiplatform") version "1.6.21" apply false
    id("com.google.cloud.tools.jib") version "3.1.4" apply false
}

ext["grpcVersion"] = "1.46.0"
ext["grpcKotlinVersion"] = "1.3.0"
ext["protobufVersion"] = "3.20.1"

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}
