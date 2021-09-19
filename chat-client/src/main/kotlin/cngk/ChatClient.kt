package cngk

import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


fun main(): Unit = runBlocking {

    val CHAT_SERVER_SERVER_TARGET = System.getenv("CHAT_SERVER_SERVER_TARGET")?.toString() ?: "localhost:50052"

    println("Connecting to " + CHAT_SERVER_SERVER_TARGET)

    val channel = ManagedChannelBuilder
            .forTarget(CHAT_SERVER_SERVER_TARGET)
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
