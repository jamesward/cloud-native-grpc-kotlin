package cngk

import io.grpc.ServerBuilder

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

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 50051

    println("Server Started: localhost:$port")

    val server = ServerBuilder
            .forPort(port)
            .addService(ExplicitFilter())
            .build()

    server.start()
    server.awaitTermination()
}
