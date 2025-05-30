package gameScreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gameScreen.wsComm.BetAction
import gameScreen.wsComm.BetPayload
import gameScreen.wsComm.Card
import gameScreen.wsComm.CardSuit
import gameScreen.wsComm.CardValue
import gameScreen.wsComm.Message
import gameScreen.wsComm.MessageType
import gameScreen.wsComm.PlayerDataToShow
import gameScreen.wsComm.PlayerInfoMessage
import kotlinx.coroutines.delay
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
import kotlin.math.roundToInt

@Composable
fun App(idTable: String, playerInfo: PlayerInfoMessage, tableTitle: String) {

    val navigator = LocalNavigator.currentOrThrow
    val listState = rememberLazyListState()

    val viewModel = remember {
        PokerViewModel(idTable, playerInfo) {
            navigator.pop()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.connect()
    }

//
//    val messages = remember { mutableStateListOf<Message>() }
//
//    var isConnected by remember { mutableStateOf(true) }
//    val scope = rememberCoroutineScope()
//    var commCards by remember { mutableStateOf(mutableListOf<Card>()) }
//    var userCards by remember { mutableStateOf(mutableListOf<Card>()) }
//
//    var hasJoined by remember { mutableStateOf(false) }
//    var handRankingText by remember { mutableStateOf("") }
//    var showCards by remember { mutableStateOf(true) }
//    var hasStartedRound by remember { mutableStateOf(false) }
//    var isReadyToPlay by remember { mutableStateOf(false) }
//    var players by remember { mutableStateOf(listOf<PlayerDataToShow>()) }
//
//    // OkHttpClient + WebSocket state
//    val client = remember {
//        OkHttpClient.Builder()
//            .pingInterval(10, TimeUnit.SECONDS)
//            .build()
//    }
//    var webSocket by remember { mutableStateOf<WebSocket?>(null) }
//
//    // Función para abrir WS, meter esto en el viewmodel
//    fun setupWebSocket() {
//        val request = Request.Builder()
//            .url("ws://localhost:8080/game/${idTable}")
//            .build()
//        webSocket = client.newWebSocket(request, object : WebSocketListener() {
//            override fun onOpen(ws: WebSocket, response: Response) {
//                scope.launch { isConnected = true }
//            }
//            override fun onMessage(ws: WebSocket, text: String) {
//                val payload = Json.decodeFromString<Message>(text)
//                scope.launch {
//
//                    if (payload.messageType == MessageType.PLAYER_READY){
//                        val (name, isReady) = Json.decodeFromString<Pair<String, Boolean>>(payload.content)
//
//                        if (name == playerInfo.name){
//                            isReadyToPlay = isReady
//                        }
//
//                    }
//                    if (payload.messageType == MessageType.STATE_UPDATE){
//                        commCards = Json.decodeFromString<MutableList<Card>>(payload.content)
//
//                        if (commCards.size == 3){
//                            commCards.add(Card(CardSuit.NONE, CardValue.NONE))
//                            commCards.add(Card(CardSuit.NONE, CardValue.NONE))
//                        }else if (commCards.size == 4){
//                            commCards.add(Card(CardSuit.NONE, CardValue.NONE))
//                        }
//
//                    }
//                    if (payload.messageType == MessageType.PLAYER_CARDS){
//                        userCards = Json.decodeFromString<MutableList<Card>>(payload.content)
//                    }
//                    if (payload.messageType == MessageType.NOTIFY_TURN) {
//                        val name = payload.content.split(":")[0]
//                        val tokens = payload.content.split(":")[1].toInt()
//
//                        println(payload.content)
//                        players.find { it.name == name }?.tokenAmount = tokens
//
//                        players = players.map {
//                            if (it.name == name) it.copy(tokenAmount = tokens) else it
//                        }
//                    }
//                    if (payload.messageType == MessageType.END_ROUND){
//                        showCards = false
//                        handRankingText = ""
//
//                        delay(800)
//
//                        isReadyToPlay = false
//                        commCards = mutableListOf<Card>()
//                        userCards = mutableListOf<Card>()
//                        hasStartedRound = false
//                    }
//                    if (payload.messageType == MessageType.HAND_RANKING){
//                        handRankingText = payload.content
//                    }
//                    if (payload.messageType == MessageType.START_ROUND){
//                        commCards = mutableListOf<Card>(
//                            Card(CardSuit.NONE, CardValue.NONE),
//                            Card(CardSuit.NONE, CardValue.NONE),
//                            Card(CardSuit.NONE, CardValue.NONE),
//                            Card(CardSuit.NONE, CardValue.NONE),
//                            Card(CardSuit.NONE, CardValue.NONE)
//                        )
//
//                        //players = Json.decodeFromString<List<PlayerDataToShow>>(payload.content)
//
//                        showCards = true
//                        hasStartedRound = true
//                    }
//                    if (payload.messageType == MessageType.SEND_PLAYER_DATA){
//                        players = Json.decodeFromString<List<PlayerDataToShow>>(payload.content)
//                    }
//                    messages.add(payload)
//
//                }
//            }
//            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
//                scope.launch {
//
//                    isConnected = false
//                    navigator.pop()
//
//                }
//            }
//            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
//                scope.launch {
//                    isConnected = false
//                    navigator.pop()
//
//                }
//            }
//        })
//}
//
//
//    // meter esto en el viewmodel
//    if (!hasJoined){
//        setupWebSocket()
//        hasJoined = true
//        val msg = Message(MessageType.PLAYER_INFO, Json.encodeToString(playerInfo))
//        webSocket?.send(Json.encodeToString(msg))
//    }

    Box{

        // Imagen de fondo
        Image(
            painter = painterResource("in_game_image.png"),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Row(modifier = Modifier.fillMaxSize()
            //.padding(16.dp)
        ) {


            // Jugadores alrededor del círculo


            // parte que enseña las cosas
            BoxWithConstraints(
                modifier = Modifier.weight(4f).fillMaxSize(),
                contentAlignment = Alignment.Center
            )
            {

                Text(
                    modifier = Modifier.align(Alignment.TopCenter),
//                    text = if (isConnected) "Connected to table: $tableTitle" else "Disconnected from table: $tableTitle",
//                    color = if (isConnected) Color(0xFF81C784) else Color(0xFFE57373),
                    text = if (viewModel.isConnected) "Connected to table: $tableTitle" else "Disconnected from table: $tableTitle",
                    color = if (viewModel.isConnected) Color(0xFF81C784) else Color(0xFFE57373),
                )

                Spacer(Modifier.height(8.dp))

//                if (hasStartedRound)
                if (viewModel.hasStartedRound)
                {
                    DrawCommCards(Modifier
//                        .fillMaxWidth(),commCards, fromBottom = false, visible = showCards)
                        .fillMaxWidth(),viewModel.commCards, fromBottom = false, visible = viewModel.showCards)

//                    DrawPlayersAroundCircle(playerInfo.name,userCards,players)
                    DrawPlayersAroundCircle(playerInfo.name,viewModel.userCards,viewModel.players)
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(8.dp)
                    )
                    {
//                        Text(handRankingText)
                        Text(viewModel.handRankingText)
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        )
                        {

                            Button(
                                onClick = {
                                    viewModel.sendAction(BetAction.RAISE,10)
//                                    val payload = BetPayload(BetAction.RAISE, 10)
//                                    val msg = Message(MessageType.ACTION, Json.encodeToString(payload))
//
//                                    viewModel.sendMessage(msg)

                                    //webSocket?.send(Json.encodeToString(msg))
                                }
                                ,
//                                enabled = isConnected,
                                enabled = viewModel.isConnected,
                                modifier = Modifier.padding(horizontal = 10.dp)) {
                                Text("Raise 10")
                            }

                            Button(onClick = {
                                viewModel.sendAction(BetAction.CALL)
//
//                                val payload = BetPayload(BetAction.CALL, 0)
//                                val msg = Message(MessageType.ACTION, Json.encodeToString(payload))
//
//                                viewModel.sendMessage(msg)

//                                webSocket?.send(Json.encodeToString(msg))
                            },
//                                enabled = isConnected,
                                enabled = viewModel.isConnected,
                                modifier = Modifier.padding(horizontal = 10.dp)) {
                                Text("Call / Check")
                            }

                            Button(onClick = {
                                viewModel.sendAction(BetAction.FOLD)

                                val payload = BetPayload(BetAction.FOLD, 0)
                                val msg = Message(MessageType.ACTION, Json.encodeToString(payload))

                                viewModel.sendMessage(msg)

//                                webSocket?.send(Json.encodeToString(msg))
                            },
//                                enabled = isConnected,
                                enabled = viewModel.isConnected,
                                modifier = Modifier.padding(horizontal = 10.dp)) {
                                Text("Fold")
                            }
                        }
                    }

                }else
                {

//                    val buttonColors = if (isReadyToPlay) {
                    val buttonColors = if (viewModel.isReadyToPlay) {
                        ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF81C784),
                            contentColor = Color.White
                        )
                    } else {
                        ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFE57373),
                            contentColor = Color.White
                        )
                    }
                    Button(modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp),
                        onClick = {

                            viewModel.sendReady()
//                            val msg = Message(MessageType.PLAYER_READY, "true")
//
//                            viewModel.sendMessage(msg)
                        //webSocket?.send(Json.encodeToString(msg))
                    },
//                        enabled = isConnected,
                        enabled = viewModel.isConnected,
                        colors = buttonColors) {
                        Text("Ready to play")
                    }
                }

            }

            // chat de la derecha
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    //.width(350.dp)
                    .weight(1f),
                //  .padding(24.dp)

                contentAlignment = Alignment.Center
            )
            {
//                    SimpleChat(listState,messages,webSocket,isConnected,
                SimpleChat(listState,viewModel.messages, { viewModel.sendMessage(it) },viewModel.isConnected,
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(Color.White.copy(alpha = 0.40f))
                )

            }

        }

        // boton de salir abajo derecha
        Button(
            onClick = {
                viewModel.disconnect()
            //webSocket?.close(4001,"")
                      },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFE57373),
                contentColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Salir",
                modifier = Modifier.size(20.dp)
            )
        }

    }


}
