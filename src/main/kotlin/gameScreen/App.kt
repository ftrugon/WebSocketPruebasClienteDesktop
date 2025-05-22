package gameScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.model.Table
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.Calendar
import java.util.concurrent.TimeUnit

class GameScreen(val idTable: String, val playerInfo: PlayerInfoMessage ) : Screen {
    @Composable
    override fun Content() {
        App(idTable, playerInfo)
    }
}


@Composable
fun App(idTable: String, playerInfo: PlayerInfoMessage) {

    val navigator = LocalNavigator.currentOrThrow

    val listState = rememberLazyListState()
    val messages = remember { mutableStateListOf<Message>() }
    var inputMessage by remember { mutableStateOf("") }
    var isConnected by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var commCards by remember { mutableStateOf(mutableListOf<Card>()) }
    var userCards by remember { mutableStateOf(mutableListOf<Card>()) }

    var hasJoined by remember { mutableStateOf(false) }

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
            .url("ws://localhost:8080/game/${idTable}")
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
                scope.launch {
                    isConnected = false
                    navigator.pop()}
            }
            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                scope.launch {
                    isConnected = false
                    navigator.pop()}
                t.printStackTrace()
            }
        })
}

    //setupWebSocket()

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

            if (!hasJoined){
                setupWebSocket()
                hasJoined = true
                val msg = Message(MessageType.PLAYER_INFO, Json.encodeToString(playerInfo))
                webSocket?.send(Json.encodeToString(msg))
                inputMessage = ""
            }

            // Row Conectar / Desconectar
            Row {
//                Button(onClick = { setupWebSocket() }) {
//                    Text("Conectar")
//                }
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
//                Button(onClick = {
//                    val msg = Message(MessageType.TEXT_MESSAGE, inputMessage)
//                    webSocket?.send(Json.encodeToString(msg))
//                    inputMessage = ""
//                }, enabled = isConnected) {
//                    Text("Enviar mensaje")
//                }
//
//                Button(onClick = {
//                    val msg = Message(MessageType.PLAYER_INFO, Json.encodeToString(playerInfo))
//                    webSocket?.send(Json.encodeToString(msg))
//                    inputMessage = ""
//                }, enabled = isConnected) {
//                    Text("Enviar nombre")
//                }

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
fun ListItem(msg: Message) {
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