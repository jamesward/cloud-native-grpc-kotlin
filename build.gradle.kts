plugins {
    id("com.google.protobuf") version "0.8.17" apply false
    kotlin("jvm") version "1.5.31" apply false
    id("com.google.cloud.tools.jib") version "3.1.4" apply false
}

ext["grpcVersion"] = "1.40.1"
ext["grpcKotlinVersion"] = "1.1.0"
ext["protobufVersion"] = "3.17.3"

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}
