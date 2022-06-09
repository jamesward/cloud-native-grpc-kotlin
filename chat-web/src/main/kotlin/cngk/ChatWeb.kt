package cngk


import io.grpc.ManagedChannelBuilder
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.webjars.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.HTML
import kotlinx.html.InputType
import kotlinx.html.LinkRel
import kotlinx.html.ScriptType
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.input
import kotlinx.html.link
import kotlinx.html.nav
import kotlinx.html.script
import kotlinx.html.ul
import java.util.*
import java.util.concurrent.Executors

// todo: close channel
// todo: serialize Message over websocket
fun Application.module() {
    install(WebSockets)
    install(Webjars)

    routing {
        val chatSessions = Collections.synchronizedSet<WebSocketSession?>(LinkedHashSet())

        val target = System.getenv("CHAT_SERVER_TARGET")?.toString() ?: "localhost:50052"
        val channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build()
        val stub = ChatGrpcKt.ChatCoroutineStub(channel)

        val chatFlow = MutableSharedFlow<Message>()

        CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher()).launch {
            stub.connect(chatFlow.asSharedFlow()).collect { message ->
                chatSessions.forEach {
                    it.send(message.body)
                }
            }
        }

        get("/") {
            call.respondHtml(block = indexHTML)
        }
        static("/") {
            staticBasePackage = "static"
            resources(".")
        }
        webSocket("/chat") {
            chatSessions += this

            try {
                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            val receivedText = frame.readText()
                            chatFlow.emit(message { body = receivedText })
                        }
                        else -> {
                            // ignored
                        }
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                chatSessions -= this
            }
        }
    }
}

fun main() {
    embeddedServer(factory = CIO, port = 8080, module = Application::module).start(wait = true)
}

val indexHTML: HTML.() -> Unit = {
    head {
        link("/webjars/bootstrap/css/bootstrap.min.css", LinkRel.stylesheet)
        link("/index.css", LinkRel.stylesheet)
        script(ScriptType.textJavaScript) {
            src = "/index.js"
        }
    }
    body {
        nav("navbar fixed-top navbar-light bg-light") {
            div("container-fluid") {
                a("/", classes = "navbar-brand") {
                    +"Chat"
                }
            }
        }

        div("container-fluid") {
            ul {
                id = "chats"
            }

            div(classes = "input-group") {
                input(InputType.text, classes = "form-control") {
                    placeholder = "message"
                    id = "message"
                }
                button(type = ButtonType.submit, classes = "btn btn-outline-secondary") {
                    id = "send"
                    +"Send"
                }
            }

        }

    }
}
