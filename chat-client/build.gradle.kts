plugins {
    application
    kotlin("jvm")
    id("com.palantir.graal") version "0.9.0"
}

dependencies {
    implementation(project(":stub-lite"))
    runtimeOnly("io.grpc:grpc-okhttp:${rootProject.ext["grpcVersion"]}")
}

application {
    mainClass.set("cngk.ChatClientKt")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

// todo: add graalvm-config-create task
// ./gradlew :chat-client:install
// JAVA_HOME=~/.gradle/caches/com.palantir.graal/21.2.0/8/graalvm-ce-java8-21.2.0 JAVA_OPTS=-agentlib:native-image-agent=config-output-dir=chat-client/src/graal chat-client/build/install/chat-client/bin/chat-client

graal {
    graalVersion("21.2.0")
    mainClass(application.mainClass.get())
    outputName("chat")
    option("--verbose")
    option("--no-server")
    option("--no-fallback")
    option("-H:+ReportExceptionStackTraces")
    option("-H:ReflectionConfigurationFiles=src/graal/reflect-config.json")
}
