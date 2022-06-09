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
    mainClass.set("cngk.ExplicitFilterKt")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

val jibExtrasDir = layout.buildDirectory.dir("jib-extras").get()

tasks.register("downloadProbe") {
    val url = "https://github.com/grpc-ecosystem/grpc-health-probe/releases/download/v0.4.5/grpc_health_probe-linux-amd64"
    jibExtrasDir.asFile.mkdirs()
    val dest = jibExtrasDir.file("grpc_health_probe").asFile
    ant.invokeMethod("get", mapOf("src" to url, "dest" to dest))
}

jib {
    extraDirectories {
        setPaths(jibExtrasDir.asFile)
        permissions = mapOf("/grpc_health_probe" to "755")
    }
}

tasks.named("jibDockerBuild") {
    dependsOn("downloadProbe")
}

tasks.named("jib") {
    dependsOn("downloadProbe")
}