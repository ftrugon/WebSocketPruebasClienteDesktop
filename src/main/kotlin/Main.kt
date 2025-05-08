import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Cliente WebSocket OkHttp",
        //resizable = false,
        // icon = ,
        state = rememberWindowState(width = 1000.dp, height = 700.dp)
    ) {
        App()
    }
}

@Composable
fun App() {
    val listState = rememberLazyListState()
    val messages = remember { mutableStateListOf<Message>() }
    var inputMessage by remember { mutableStateOf("") }
    var isConnected by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var commCards by remember { mutableStateOf(mutableListOf<Card>()) }
    var userCards by remember { mutableStateOf(mutableListOf<Card>()) }
    // OkHttpClient + WebSocket state
    val client = remember {
        OkHttpClient.Builder()
            .pingInterval(10, TimeUnit.SECONDS)
            .build()
    }
    var webSocket by remember { mutableStateOf<WebSocket?>(null) }

    // Función para abrir WS
    fun setupWebSocket() {
        val request = Request.Builder()
            .url("ws://localhost:8080/game/2")
            .build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                scope.launch { isConnected = true }
            }
            override fun onMessage(ws: WebSocket, text: String) {
                val payload = Json.decodeFromString<Message>(text)
                scope.launch {

                    if (payload.messageType == MessageType.STATE_UPDATE){
                        commCards = Json.decodeFromString<MutableList<Card>>(payload.content)
                    }
                    if (payload.messageType == MessageType.PLAYER_CARDS){
                        userCards = Json.decodeFromString<MutableList<Card>>(payload.content)
                    }
                    if (payload.messageType == MessageType.END_ROUND){
                        commCards = mutableListOf<Card>()
                        userCards = mutableListOf<Card>()
                    }

                    messages.add(payload)

                }
            }
            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                scope.launch { isConnected = false }
            }
            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                scope.launch { isConnected = false }
                t.printStackTrace()
            }
        })
    }

    Row(modifier = Modifier.fillMaxSize()
        //.padding(16.dp)
    ) {

        Column(
            modifier = Modifier.weight(3f).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(if (isConnected) "Conectado" else "Desconectado")
            Spacer(Modifier.height(8.dp))

            // Row Conectar / Desconectar
            Row {
                Button(onClick = { setupWebSocket() }) {
                    Text("Conectar")
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    webSocket?.close(1000, "Usuario cerró la conexión")
                }) {
                    Text("Desconectar")
                }
            }

            Spacer(Modifier.height(16.dp))

            // Campo de texto genérico
            OutlinedTextField(
                value = inputMessage,
                onValueChange = { inputMessage = it },
                label = { Text("Mensaje") },
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            // Botones de acción
            Row {
                Button(onClick = {
                    val msg = Message(MessageType.TEXT_MESSAGE, inputMessage)
                    webSocket?.send(Json.encodeToString(msg))
                    inputMessage = ""
                }, enabled = isConnected) {
                    Text("Enviar mensaje")
                }

                Button(onClick = {
                    val player = PlayerInfoMessage(inputMessage, 100)
                    val msg = Message(MessageType.PLAYER_INFO, Json.encodeToString(player))
                    webSocket?.send(Json.encodeToString(msg))
                    inputMessage = ""
                }, enabled = isConnected) {
                    Text("Enviar nombre")
                }

                Button(onClick = {
                    val msg = Message(MessageType.PLAYER_READY, "true")
                    webSocket?.send(Json.encodeToString(msg))
                }, enabled = isConnected) {
                    Text("Listo para jugar")
                }
            }



            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp), // Altura para mostrar cartas
                horizontalArrangement = Arrangement.Center
            ) {
                // Aquí puedes mostrar tus cartas
                DrawCards(commCards)
            }

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Button(
                    onClick = {
                        val payload = BetPayload(BetAction.RAISE, 10)
                        val msg = Message(MessageType.ACTION, Json.encodeToString(payload))
                        webSocket?.send(Json.encodeToString(msg)) }
                    ,
                    enabled = isConnected,
                    modifier = Modifier.padding(horizontal = 10.dp)) {
                    Text("Raise 10")
                }

                Button(onClick = {
                    val payload = BetPayload(BetAction.CALL, 0)
                    val msg = Message(MessageType.ACTION, Json.encodeToString(payload))
                    webSocket?.send(Json.encodeToString(msg))
                }, enabled = isConnected,
                    modifier = Modifier.padding(horizontal = 10.dp)) {
                    Text("Call / Check")
                }

                Button(onClick = {
                    val payload = BetPayload(BetAction.FOLD, 0)
                    val msg = Message(MessageType.ACTION, Json.encodeToString(payload))
                    webSocket?.send(Json.encodeToString(msg))
                }, enabled = isConnected,
                    modifier = Modifier.padding(horizontal = 10.dp)) {
                    Text("Fold")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp), // Altura para mostrar cartas
                horizontalArrangement = Arrangement.Center
            ) {
                DrawCards(userCards)
            }


        }


        Row(Modifier.weight(1f).fillMaxHeight().background(Color.Green)) {
            SimpleChat(listState,messages,webSocket,isConnected,)
        }

    }
}

@Composable
fun DrawCards(cardList: List<Card>){
    for (card in cardList){
        DrawCard(card)
    }
}

@Composable
fun DrawCard(card: Card) {


    val simbolToWrite:String
    val colorOfCard:Color = when (card.suit) {
        CardSuit.DIAMONDS -> {
            simbolToWrite = "♦"
            Color.Red
        }
        CardSuit.SPADES -> {
            simbolToWrite = "♠"
            Color.Black
        }
        CardSuit.HEARTS -> {
            simbolToWrite = "♥"
            Color.Red
        }
        CardSuit.CLUBS -> {
            simbolToWrite = "♣"
            Color.Black
        }
    }

    val textOfCard:String = when (card.value){

        CardValue.J -> "J"
        CardValue.Q -> "Q"
        CardValue.K -> "K"
        CardValue.AS -> "AS"
        else -> card.value.weight.toString()
    }

    Box(
        modifier = Modifier
            .size(60.dp, 90.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, colorOfCard,RoundedCornerShape(8.dp))
            .padding(4.dp)
    ) {

        Text("$textOfCard $simbolToWrite",color = colorOfCard)
    }
    Spacer(modifier = Modifier.width(8.dp))
}


@Composable
fun SimpleChat(
    listState: LazyListState,
    messages: List<Message>,
    webSocket: WebSocket?,
    isConnected: Boolean,
    modifier: Modifier = Modifier
) {

    var inputMessage by remember { mutableStateOf("") }


    Column {
        LazyColumn(
            state = listState,
            modifier = modifier.weight(1f),
            // reverseLayout = true
        ) {
            items(messages) {
                when (it.messageType) {
                    MessageType.TEXT_MESSAGE -> {
                        ListItem(it)
                    }
                    MessageType.PLAYER_READY -> {
                        Text("${it.content} is ready to play.")
                    }
                    MessageType.PLAYER_JOIN -> {
                        Text("${it.content} has joined the table.")
                    }
                    else -> {
                        Text(it.content)
                    }
                }

            }
        }

        LaunchedEffect(messages.size) {
            val index = if (messages.isEmpty()) 0 else messages.size - 1
            listState.scrollToItem(index)
        }

        OutlinedTextField(
            value = inputMessage,
            onValueChange = { inputMessage = it },
            label = { Text("Mensaje") },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = {
                    val msg = Message(MessageType.TEXT_MESSAGE, inputMessage)
                    webSocket?.send(Json.encodeToString(msg))
                    inputMessage = ""
                },enabled = isConnected) {
                    Icon(Icons.Default.Send, contentDescription = "")
                }
            }
        )


    }
}

@Composable
fun ListItem(msg:Message) {
    Box(
        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color(0xFFDDEEFF),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp)  // padding interior uniforme
                .fillMaxWidth()
        ) {
            Text(
                text = msg.content,
                color = Color.Black,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timestamp = String.format("%02d:%02d", hour, minute)

            Text(
                text = timestamp,
                fontSize = 12.sp,
                color = Color.DarkGray,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp)
            )
        }
    }
}