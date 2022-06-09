plugins {
    id("com.google.protobuf") version "0.8.18" apply false
    kotlin("jvm") version "1.7.0" apply false
    id("com.google.cloud.tools.jib") version "3.2.1" apply false
}

ext["grpcVersion"] = "1.47.0"
ext["grpcKotlinVersion"] = "1.3.0"
ext["protobufVersion"] = "3.21.1"

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}