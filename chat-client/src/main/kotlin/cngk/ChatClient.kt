package cngk

import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

suspend fun connect(target: String) = run {
    println("Connecting to $target")

    val channel = ManagedChannelBuilder
        .forTarget(target)
        .usePlaintext()
        .build()

    val client = ChatGrpcKt.ChatCoroutineStub(channel)

    val chatFlow = flow {
        while (true) {
            print("> ")
            readLine()?.let {
                emit(message { body = it })
            }
        }
    }

    client.connect(chatFlow.flowOn(Dispatchers.IO)).collect {
        println("")
        println("< " + it.body)
        print("> ")
    }
}

fun main(): Unit = runBlocking {
    val target = System.getenv("CHAT_SERVER_TARGET")?.toString() ?: "localhost:50052"
    connect(target)
}
