package cngk

import io.grpc.ServerBuilder
import kotlin.math.pow
import kotlin.math.roundToLong
import io.grpc.health.v1.*

class ExplicitFilter : ExplicitFilterGrpcKt.ExplicitFilterCoroutineImplBase() {

    val badWords = setOf("foo", "bar")

    override suspend fun applyFilter(request: Text): Text {
        val filtered = request.body.split(Regex("\\s")).map { word ->
            if (badWords.find { it == word.lowercase() } != null) {
                word.map { "*" }.joinToString("")
            }
            else if (word.startsWith("bomb")) {
                HealthCheck.hc_status = HealthCheckResponse.ServingStatus.NOT_SERVING
            }
            else if (word.startsWith("exit")) {
                System.exit(1)
            }
            else {
                word
            }
        }

        return text { body = filtered.joinToString(" ") }
    }

}

class HealthCheck : HealthGrpcKt.HealthCoroutineImplBase() {
    override suspend fun check(request: HealthCheckRequest): HealthCheckResponse {
        return healthCheckResponse {
            status = hc_status
        }
    }

    companion object {
        var hc_status = HealthCheckResponse.ServingStatus.SERVING
    }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 50051

    println("Server Started: localhost:$port")

    val server = ServerBuilder
            .forPort(port)
            .addService(ExplicitFilter())
            .addService(HealthCheck())
            .build()

    server.start()
    server.awaitTermination()
}
