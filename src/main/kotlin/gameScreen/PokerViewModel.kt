package gameScreen

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

class PokerViewModel(
    private val idTable: String,
    private val playerInfo: PlayerInfoMessage,
    private val onDisconnected: () -> Unit
) {
    private val client = OkHttpClient.Builder()
        .pingInterval(10, TimeUnit.SECONDS)
        .build()

    var webSocket: WebSocket? = null
    var isConnected by mutableStateOf(false)
    var commCards by mutableStateOf(mutableListOf<Card>())
    var userCards by mutableStateOf(mutableListOf<Card>())
    var handRankingText by mutableStateOf("")
    var showCards by mutableStateOf(true)
    var hasStartedRound by mutableStateOf(false)
    var isReadyToPlay by mutableStateOf(false)
    var players by mutableStateOf(listOf<PlayerDataToShow>())
    var messages = mutableStateListOf<Message>()

    private var hasJoined = false

    fun connect() {
        if (hasJoined) return
        hasJoined = true

        val request = Request.Builder()
            .url("ws://localhost:8080/game/$idTable")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                isConnected = true
                sendMessage(Message(MessageType.PLAYER_INFO, Json.encodeToString(playerInfo)))
            }

            override fun onMessage(ws: WebSocket, text: String) {
                val payload = Json.decodeFromString<Message>(text)
                handleMessage(payload)
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                isConnected = false
                onDisconnected()
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                isConnected = false
                onDisconnected()
            }
        })
    }

    private fun handleMessage(payload: Message) {
        when (payload.messageType) {
            MessageType.PLAYER_READY -> {
                val (name, ready) = Json.decodeFromString<Pair<String, Boolean>>(payload.content)
                if (name == playerInfo.name) isReadyToPlay = ready
            }

            MessageType.STATE_UPDATE -> {
                commCards = Json.decodeFromString(payload.content)
                while (commCards.size < 5) {
                    commCards.add(Card(CardSuit.NONE, CardValue.NONE))
                }
            }

            MessageType.PLAYER_CARDS -> {
                userCards = Json.decodeFromString(payload.content)
            }

            MessageType.NOTIFY_TURN -> {
                val (name, tokens) = payload.content.split(":").let { it[0] to it[1].toInt() }
                players = players.map {
                    if (it.name == name) it.copy(tokenAmount = tokens) else it
                }
            }

            MessageType.END_ROUND -> {
                showCards = false
                handRankingText = ""

                CoroutineScope(Dispatchers.IO).launch {

                    delay(800)

                    isReadyToPlay = false
                    commCards = mutableListOf()
                    userCards = mutableListOf()
                    hasStartedRound = false

                }
            }

            MessageType.HAND_RANKING -> handRankingText = payload.content
            MessageType.START_ROUND -> {
                commCards = MutableList(5) { Card(CardSuit.NONE, CardValue.NONE) }
                showCards = true
                hasStartedRound = true
            }

            MessageType.SEND_PLAYER_DATA -> {
                players = Json.decodeFromString(payload.content)
            }

            else -> {}
        }

        messages.add(payload)
    }

    fun sendReady() {
        sendMessage(Message(MessageType.PLAYER_READY, "true"))
    }

    fun sendAction(action: BetAction, amount: Int = 0) {
        val payload = BetPayload(action, amount)
        sendMessage(Message(MessageType.ACTION, Json.encodeToString(payload)))
    }

    fun sendMessage(message: Message) {
        webSocket?.send(Json.encodeToString(message))
    }

    fun disconnect() {
        webSocket?.close(4001, "")
    }
}
