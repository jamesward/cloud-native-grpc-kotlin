package cngk

import io.grpc.ServerBuilder
import io.grpc.health.v1.HealthCheckResponse
import io.grpc.protobuf.services.HealthStatusManager
import kotlin.system.exitProcess

class ExplicitFilter : ExplicitFilterGrpcKt.ExplicitFilterCoroutineImplBase() {

    val badWords = setOf("foo", "bar")

    override suspend fun applyFilter(request: Text): Text {
        val filtered = request.body.split(Regex("\\s")).map { word ->
            if (badWords.find { it == word.lowercase() } != null) {
                word.map { "*" }.joinToString("")
            }
            else {
                word
            }
        }

        return text { body = filtered.joinToString(" ") }
    }

}

val healthStatusManager = HealthStatusManager()

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 50051

    println("Server Started: localhost:$port")

    // https://grpc.github.io/grpc-java/javadoc/io/grpc/protobuf/services/HealthStatusManager.html
    healthStatusManager.setStatus(HealthStatusManager.SERVICE_NAME_ALL_SERVICES, HealthCheckResponse.ServingStatus.SERVING)

    val server = ServerBuilder
            .forPort(port)
            .addService(ExplicitFilter())
            .addService(healthStatusManager.healthService)
            .build()

    server.start()
    server.awaitTermination()
}
