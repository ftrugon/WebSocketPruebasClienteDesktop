import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Cliente WebSocket OkHttp") {
        App()
    }
}

@Preview
@Composable
fun App() {
    var messages by remember { mutableStateOf(listOf<String>()) }
    var inputMessage by remember { mutableStateOf("") }
    var isConnected by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val client = remember {
        OkHttpClient.Builder()
            .pingInterval(10, TimeUnit.SECONDS) // Opcional: para mantener viva la conexión
            .build()
    }

    var webSocket by remember { mutableStateOf<WebSocket?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(if (isConnected) "Conectado" else "Desconectado")
        Spacer(Modifier.height(8.dp))

        Row {
            Button(onClick = {
                val request = Request.Builder()
                    .url("ws://localhost:8080/game/1") // Cambia tu URL aquí
                    .build()

                webSocket = client.newWebSocket(request, object : WebSocketListener() {
                    override fun onOpen(ws: WebSocket, response: Response) {
                        isConnected = true
                    }

                    override fun onMessage(ws: WebSocket, text: String) {
                        messages = messages + text
                    }

                    override fun onMessage(ws: WebSocket, bytes: ByteString) {
                        messages = messages + bytes.hex()
                    }

                    override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                        isConnected = false
                    }

                    override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                        isConnected = false
                        t.printStackTrace()
                    }
                })

            }) {
                Text("Conectar")
            }

            Spacer(Modifier.width(8.dp))

            Button(onClick = {
                webSocket?.close(1000, "Usuario cerró la conexión")
                isConnected = false
            }) {
                Text("Desconectar")
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = inputMessage,
            onValueChange = { inputMessage = it },
            label = { Text("Mensaje") }
        )

        Button(onClick = {
            webSocket?.send(inputMessage)
            inputMessage = ""
        }, enabled = isConnected) {
            Text("Enviar")
        }

        Spacer(Modifier.height(16.dp))

        Text("Mensajes recibidos:")
        Column {
            messages.forEach {
                Text(it)
            }
        }
    }
}
