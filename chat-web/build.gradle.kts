import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
    kotlin("multiplatform")
}

/*
dependencies {
    implementation("pro.streem.pbandk:pbandk-runtime:0.14.0")
    implementation(npm("protobufjs", "6.11.3"))
}
 */

kotlin {
    js(IR) {
        browser {
            runTask {
                devServer = devServer?.copy(port = 8081, proxy = mutableMapOf("/api" to "http://localhost:8080"))
            }
        }
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(project(":stub-kmp"))
            }
        }
    }
    /*
    js(IR) {
        binaries.executable()
        browser {}
    }

    sourceSets {
        main {
            //kotlin.srcDir("${project(":lib-proto").buildDir}/generated/source/proto/main/pbandk")
        }
    }
     */
}

tasks {
    /*
    val compileKotlinJs by getting(KotlinJsCompile::class) {
        kotlinOptions {
            moduleKind = "umd"
            freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
     */

    /*
    // Generate the protobuf files before compilation
    project(":lib-proto").tasks
        .matching { it.name == "generateProto" }
        .all { compileKotlinJs.dependsOn(this) }
     */
}

/*
// Workaround for build failure. See https://youtrack.jetbrains.com/issue/KT-49124.
rootProject.extensions.configure<NodeJsRootExtension> {
    versions.webpackCli.version = "4.9.0"
}
 */