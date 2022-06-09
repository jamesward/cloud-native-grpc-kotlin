plugins {
    application
    kotlin("jvm")
    id("com.google.cloud.tools.jib")
}

dependencies {
    implementation(project(":stub"))
    implementation("io.ktor:ktor-server-core:2.0.2")
    implementation("io.ktor:ktor-server-cio:2.0.2")
    implementation("io.ktor:ktor-server-html-builder:2.0.2")
    implementation("io.ktor:ktor-server-websockets:2.0.2")
    implementation("io.ktor:ktor-server-webjars:2.0.2")
    runtimeOnly("org.webjars:bootstrap:5.1.3")
    implementation("org.jetbrains.kotlinx:kotlinx-html:0.7.5")
    runtimeOnly("io.grpc:grpc-netty:${rootProject.ext["grpcVersion"]}")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.11")
}

application {
    mainClass.set("cngk.ChatWebKt")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}