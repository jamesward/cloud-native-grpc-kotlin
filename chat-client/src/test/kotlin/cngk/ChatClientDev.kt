package cngk

import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.output.Slf4jLogConsumer

class EFServer : GenericContainer<EFServer>("ef-server") {
    val port = 50051

    init {
        withEnv("PORT", port.toString())
        withExposedPorts(port)
    }
}

class ChatServer : GenericContainer<ChatServer>("chat-server")

fun main() = runBlocking {

    val network = Network.newNetwork()

    val logger = LoggerFactory.getLogger(this::class.java)

    val logConsumer = Slf4jLogConsumer(logger)

    val efServer = EFServer().withNetwork(network)
    efServer.start()
    efServer.followOutput(logConsumer)

    val efTarget = efServer.networkAliases.first() + ":" + efServer.port
    println("EF Server: $efTarget")

    val chatServer = ChatServer()
        .withNetwork(network)
        .withExposedPorts(50052)
        .withEnv("EF_SERVER_TARGET", efTarget)
    chatServer.start()
    chatServer.followOutput(logConsumer)

    val chatTarget = chatServer.host + ":" + chatServer.firstMappedPort
    println("Chat Server: $chatTarget")
    connect(chatTarget)

    efServer.stop()
    chatServer.stop()

}
