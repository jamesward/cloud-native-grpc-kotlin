package cngk

import io.grpc.ServerBuilder
import kotlin.math.pow
import kotlin.math.roundToLong

class ExplicitFilter : ExplicitFilterGrpcKt.ExplicitFilterCoroutineImplBase() {

    val badWords = setOf("foo", "bar")

    override suspend fun applyFilter(request: Text): Text {
        val filtered = request.body.split(Regex("\\s")).map { word ->
            if (badWords.find { it == word.lowercase() } != null) {
                word.map { "*" }.joinToString("")
            }
            else if (word.startsWith("block:")) {
                try {
                    val input = word.subSequence(6,word.length).toString().toLong()
                    val out = intenseFunc(input)
                    out
                } catch (nfe: NumberFormatException) {
                    "Not a valid double"
                }

            }
            else {
                word
            }
        }



        return text { body = filtered.joinToString(" ") }
    }

    fun intenseFunc(input:Long):Long {
        var result = 0L

        val superIntense = input%2 == 0L
        println("Intense starting with super intense set to: $superIntense")

        for (indx in input downTo 0 step 1) {
            val next = indx.toDouble().pow(27)
            if ( superIntense) {
                result += Math.atan(Math.tan(Math.atan(Math.tan(Math.atan(Math.tan(next)))))).roundToLong()
            }
            else {
                result += Math.atan(Math.tan(Math.atan(next))).roundToLong()
            }

            if (indx % 1000000 == 0L) {
                println("Still calculating - current result $result")
            }

        }

        println("Intense finished")
        return result;
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
