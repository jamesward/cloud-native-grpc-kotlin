package cngk

import io.grpc.ManagedChannelBuilder
import io.grpc.ServerBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Chat(val explicitFilter: ExplicitFilterGrpcKt.ExplicitFilterCoroutineStub) : ChatGrpcKt.ChatCoroutineImplBase() {

    private val sharedFlow = MutableSharedFlow<Message>()

    override fun connect(requests: Flow<Message>): Flow<Message> {
        val filteredFlow: Flow<Message> = requests.map { inMessage ->
            val filtered = explicitFilter.applyFilter(text { body = inMessage.body })
            message { body = filtered.body }
        }

        CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher()).launch {
            sharedFlow.emitAll(filteredFlow)
        }

        return sharedFlow
    }
}

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 50052

    val channel = ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext().build()

    val client = ExplicitFilterGrpcKt.ExplicitFilterCoroutineStub(channel)

    val server = ServerBuilder
            .forPort(port)
            .addService(Chat(client))
            .build()

    println("Starting Server: localhost:$port")

    server.start()
    server.awaitTermination()

    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
}
