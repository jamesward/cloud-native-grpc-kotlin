plugins {
    application
    kotlin("jvm")
    id("com.google.cloud.tools.jib")
}

dependencies {
    implementation(project(":stub"))
    runtimeOnly("io.grpc:grpc-netty:${rootProject.ext["grpcVersion"]}")
}

application {
    mainClass.set("cngk.ChatServerKt")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}