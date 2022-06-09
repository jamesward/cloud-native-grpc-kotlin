import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc
import com.google.protobuf.gradle.remove

plugins {
    `java-library`
    kotlin("multiplatform")
    id("com.google.protobuf")
}

dependencies {
    protobuf(project(":protos"))

    compileOnly("com.google.protobuf:protobuf-java:${rootProject.ext["protobufVersion"]}")
}

kotlin {
    js(IR) {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("$buildDir/generated/source/proto/main/pbandk")
            dependencies {
                implementation("pro.streem.pbandk:pbandk-runtime:0.14.0")
            }
        }
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${rootProject.ext["protobufVersion"]}"
    }
    plugins {
        id("pbandk") {
            artifact = "pro.streem.pbandk:protoc-gen-pbandk-jvm:0.14.0:jvm8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("pbandk")
            }
            it.builtins {
                remove("java")
            }
        }
    }
}

tasks {
    compileJava {
        enabled = false
    }
}

tasks.named("compileKotlinJs") {
    dependsOn("generateProto")
}