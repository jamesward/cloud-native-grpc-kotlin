package cngk

import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


fun main(): Unit = runBlocking {

    val channel = ManagedChannelBuilder
            .forAddress("localhost", 50052)
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
