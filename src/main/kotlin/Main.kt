import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import java.util.concurrent.TimeUnit
import kotlin.math.cos
import kotlin.math.sin

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
                    .url("ws://localhost:8080/game/2") // Cambia tu URL aquí
                    .build()

                webSocket = client.newWebSocket(request, object : WebSocketListener() {
                    override fun onOpen(ws: WebSocket, response: Response) {
                        isConnected = true
                    }

                    override fun onMessage(ws: WebSocket, text: String) {

                        val messagePayload = Json.decodeFromString<Message>(text)


                        messages = messages +( messagePayload.content + " ---> " + messagePayload.messageType )

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
            val message = Message(MessageType.TEXT_MESSAGE, inputMessage)
            val jsonMessage = Json.encodeToString<Message>(message)
            webSocket?.send(jsonMessage)
            inputMessage = ""
        }, enabled = isConnected) {
            Text("Enviar mensaje")
        }

        Button(onClick = {
            val playerinfo = PlayerInfoMessage(inputMessage,100)
            val plterinfjson = Json.encodeToString(playerinfo)
            val message = Message(MessageType.PLAYER_INFO, plterinfjson)
            val jsonMessage = Json.encodeToString<Message>(message)
            webSocket?.send(jsonMessage)
            inputMessage = ""
        }, enabled = isConnected) {
            Text("Enviar nombre")
        }



        Button(onClick = {
            val message = Message(MessageType.PLAYER_READY, "true")
            val jsonInfo = Json.encodeToString<Message>(message)
            webSocket?.send(jsonInfo)
            inputMessage = ""
        }, enabled = isConnected) {
            Text("Listo para jugar")
        }

        Button(onClick = {
            val betTal = BetPayload(BetAction.CALL,0)
            val jsonBet = Json.encodeToString(betTal)
            val message = Message(MessageType.ACTION, jsonBet)
            val jsonInfo = Json.encodeToString<Message>(message)
            webSocket?.send(jsonInfo)
            inputMessage = ""
        }, enabled = isConnected) {
            Text("Enviar informacion")
        }


        Spacer(Modifier.height(16.dp))

        Text("Mensajes recibidos:")

        SimpleChat(messages)

    }
}


@Composable
fun SimpleChat(messages: List<String>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier,
        reverseLayout = true
    ) {
        items(messages) { message ->
            Text(
                text = message,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}



@Composable
fun PokerTable(players: List<String>, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .aspectRatio(1f) // Para que sea un cuadrado (y parezca un círculo)
            .background(Color(0xFF2E7D32), CircleShape)
    ) {
        players.forEachIndexed { index, player ->
            val angle = (360f / players.size) * index - 90f // Empezar arriba
            val density = LocalDensity.current
            val radius = with(density) { 150.dp.toPx() }

            Box(
                modifier = Modifier
                    .offset {
                        // Convertimos ángulo a coordenadas
                        val rad = Math.toRadians(angle.toDouble())
                        val x = (cos(rad) * radius).toInt()
                        val y = (sin(rad) * radius).toInt()
                        IntOffset(x, y)
                    }
                    .size(60.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(player, color = Color.Black)
            }
        }
    }
}
