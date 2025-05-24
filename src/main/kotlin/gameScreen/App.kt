package gameScreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
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
import data.model.Table
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


class GameScreen(val idTable: String, val playerInfo: PlayerInfoMessage,val tableTitle: String) : Screen {
    @Composable
    override fun Content() {
        App(idTable, playerInfo,tableTitle)
    }
}


@Composable
fun App(idTable: String, playerInfo: PlayerInfoMessage,tableTitle: String) {

    val navigator = LocalNavigator.currentOrThrow

    val listState = rememberLazyListState()
    val messages = remember { mutableStateListOf<Message>() }
    var isConnected by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    var commCards by remember { mutableStateOf(mutableListOf<Card>()) }
    var userCards by remember { mutableStateOf(mutableListOf<Card>()) }

    var hasJoined by remember { mutableStateOf(false) }
    var handRankingText by remember { mutableStateOf("") }
    var showCards by remember { mutableStateOf(false) }
    var hasStartedRound by remember { mutableStateOf(false) }
    var isReadyToPlay by remember { mutableStateOf(false) }

    // OkHttpClient + WebSocket state
    val client = remember {
        OkHttpClient.Builder()
            .pingInterval(10, TimeUnit.SECONDS)
            .build()
    }
    var webSocket by remember { mutableStateOf<WebSocket?>(null) }

    // Función para abrir WS, meter esto en el viewmodel
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

                    if (payload.messageType == MessageType.PLAYER_READY){
                        val (name, isReady) = Json.decodeFromString<Pair<String, Boolean>>(payload.content)

                        if (name == playerInfo.name){
                            isReadyToPlay = isReady
                        }

                    }
                    if (payload.messageType == MessageType.STATE_UPDATE){
                        commCards = Json.decodeFromString<MutableList<Card>>(payload.content)

                        if (commCards.size == 3){
                            commCards.add(Card(CardSuit.NONE, CardValue.NONE))
                            commCards.add(Card(CardSuit.NONE, CardValue.NONE))
                        }else if (commCards.size == 4){
                            commCards.add(Card(CardSuit.NONE, CardValue.NONE))
                        }

                    }
                    if (payload.messageType == MessageType.PLAYER_CARDS){
                        userCards = Json.decodeFromString<MutableList<Card>>(payload.content)
                    }
                    if (payload.messageType == MessageType.END_ROUND){
                        showCards = false
                        handRankingText = ""

                        delay(800)

                        commCards = mutableListOf<Card>()
                        userCards = mutableListOf<Card>()
                        hasStartedRound
                    }
                    if (payload.messageType == MessageType.HAND_RANKING){
                        handRankingText = payload.content
                    }
                    if (payload.messageType == MessageType.START_ROUND){
                        commCards = mutableListOf<Card>(
                            Card(CardSuit.NONE, CardValue.NONE),
                            Card(CardSuit.NONE, CardValue.NONE),
                            Card(CardSuit.NONE, CardValue.NONE),
                            Card(CardSuit.NONE, CardValue.NONE),
                            Card(CardSuit.NONE, CardValue.NONE)
                        )
                        showCards = true
                        hasStartedRound = true
                    }
                    messages.add(payload)

                }
            }
            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                scope.launch {

                    isConnected = false
                    navigator.pop()

                }
            }
            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                scope.launch {
                    isConnected = false
                    navigator.pop()

                }
            }
        })
}


    Box(){

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

            Column(
                modifier = Modifier.weight(3f).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Text(
                    text = if (isConnected) "Connected to table: $tableTitle" else "Disconnected from table: $tableTitle",
                    color = if (isConnected) Color(0xFF81C784) else Color(0xFFE57373),
                )
                Spacer(Modifier.height(8.dp))


                // meter esto en el viewmodel
                if (!hasJoined){
                    setupWebSocket()
                    hasJoined = true
                    val msg = Message(MessageType.PLAYER_INFO, Json.encodeToString(playerInfo))
                    webSocket?.send(Json.encodeToString(msg))
                }


                DrawCards(Modifier
                    .fillMaxWidth(),commCards, fromBottom = false, visible = showCards)


                if (hasStartedRound)
                {
                    Column {
                        Text(handRankingText)
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        )
                        {

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
                    }

                }else
                {

                    val buttonColors = if (isReadyToPlay) {
                        ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF81C784), // Verde menta
                            contentColor = Color.White
                        )
                    } else {
                        ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFE57373), // Rojo coral suave
                            contentColor = Color.White
                        )
                    }
                    Button(onClick = {
                        val msg = Message(MessageType.PLAYER_READY, "true")
                        webSocket?.send(Json.encodeToString(msg))
                    },
                        enabled = isConnected,
                        colors = buttonColors) {
                        Text("ReadyToPlay")
                    }
                }

                DrawCards(Modifier
                    .fillMaxWidth(),userCards, fromBottom = true, visible = showCards)


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

                SimpleChat(listState,messages,webSocket,isConnected,
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(Color.White.copy(alpha = 0.40f))
                )

            }


        }


        Button(
            onClick = { webSocket?.close(4001,"") },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFE57373),
                contentColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Salir",
                modifier = Modifier.size(20.dp)
            )
        }

    }


}

@Composable
fun DrawCards(
    modifier: Modifier = Modifier,
    cardList: List<Card>,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 800, easing = FastOutSlowInEasing),
    fromBottom: Boolean = true,
    visible: Boolean = true // controla si está visible o no
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val density = LocalDensity.current
        val offsetY = remember { Animatable(0f) }

        LaunchedEffect(cardList, visible) {
            val offScreenOffset = with(density) {
                if (fromBottom) maxHeight.toPx() else -maxHeight.toPx()
            }

            if (visible) {
                // Aparece: animar desde fuera hacia 0
                offsetY.snapTo(offScreenOffset)
                offsetY.animateTo(0f, animationSpec)
            } else {
                // Desaparece: animar desde 0 hacia fuera
                offsetY.snapTo(0f)
                offsetY.animateTo(offScreenOffset, animationSpec)
            }
        }

        Row(
            modifier = Modifier
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            for (card in cardList) {
                DrawCard(card)
            }
        }
    }
}


@Composable
fun DrawCard(card: Card) {


    val colorOfCard:Color = when (card.suit) {
        CardSuit.DIAMONDS -> {
            Color.Red
        }
        CardSuit.SPADES -> {
            Color.Black
        }
        CardSuit.HEARTS -> {
            Color.Red
        }
        CardSuit.CLUBS -> {
            Color.Black
        }

        CardSuit.NONE -> {
            Color.Transparent
        }

    }

    var imageUrl:String = ""

    if (card.suit != CardSuit.NONE) {
        val cardSuitString = card.suit.toString().lowercase()
        val cardValueWeight = if (card.value == CardValue.AS) 1 else card.value.weight.toString()
        imageUrl = "/cardsImages/$cardSuitString/${cardSuitString}_$cardValueWeight.png"
    }else{
        imageUrl = "/cardsImages/card.png"
    }

    Box(
        modifier = Modifier
            .size(80.dp, 120.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(2.dp, colorOfCard, RoundedCornerShape(8.dp))
            //.padding(4.dp)
    ) {
        Image(
            painter = painterResource(imageUrl),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
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

    // meter la parte de escribir en el viewmodel
    var inputMessage by remember { mutableStateOf("") }

    Column(modifier = modifier) {
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
                        val (name, isReady) = Json.decodeFromString<Pair<String, Boolean>>(it.content)
                        Text("$name is ${if (isReady) "ready" else "not ready" } to play.")

                    }
                    MessageType.PLAYER_JOIN -> {
                        Text("${it.content} has joined the table.")
                    }
                    MessageType.PLAYER_LEAVE -> {
                        Text("${it.content} has leaved the table.")
                    }
                    MessageType.SERVER_RESPONSE -> {
                        Text(it.content)
                    }
                    MessageType.NOTIFY_WINNER -> {
                        Text(it.content)
                    }
                    MessageType.NOTIFY_TURN -> {
                        Text(it.content)
                    }
                    else -> {

                    }
                }
            }
        }

        LaunchedEffect(messages.size) {
            val index = if (messages.isEmpty()) 0 else messages.size - 1
            listState.scrollToItem(index)
        }

        TextField(
            value = inputMessage,
            onValueChange = { inputMessage = it },
            label = { Text("Send message") },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = {
                val msg = Message(MessageType.TEXT_MESSAGE, inputMessage)
                webSocket?.send(Json.encodeToString(msg))
                inputMessage = ""
            },enabled = isConnected) {
                Icon(Icons.Default.Send, contentDescription = "")
            }}
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