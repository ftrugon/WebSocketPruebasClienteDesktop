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


/**
 * clase para el viewmodel de la paartida del pokler
 * @property idTable la id de la tabla a la que se une el cliente
 * @property playerInfo la informacion del jugador que se une
 * @property onDisconnected la funcion que se produce cuando se desconecta el usuario
 */
class PokerViewModel(
    private val idTable: String,
    private val playerInfo: PlayerInfoMessage,
    private val onDisconnected: () -> Unit
) {

    // url con la que te coneectas
    private val url = "ws://localhost:8080/game/"

    // el cliente que hace la conexion con el ws
    private val client = OkHttpClient.Builder()
        .pingInterval(10, TimeUnit.SECONDS)
        .build()

    var webSocket: WebSocket? = null // el websocket que hace la conexion
    var isConnected by mutableStateOf(false) // si esta conectado para manejar algunas cosas en la interfaz
    var commCards by mutableStateOf(mutableListOf<Card>()) // cartas comunitarias que se ddibujan
    var userCards by mutableStateOf(mutableListOf<Card>()) // las cartas del usuario
    var handRankingText by mutableStateOf("") // el texto del ranking
    var actualDeskAmount by mutableStateOf("") // texto dde la cantidadd actual de dinero en la mesa
    var showCards by mutableStateOf(true) // mostrar las cartas o no
    var hasStartedRound by mutableStateOf(false) // ha empezado la  ronda o no
    var isReadyToPlay by mutableStateOf(false) // el jugadore esta listo para jugar
    var players by mutableStateOf(mutableListOf<PlayerDataToShow>()) // jfguaadores en la mesa
    var messages by mutableStateOf(mutableListOf<Message>()) // mensages enviados
    private var hasJoined = false // se ha uniddo o no

    /**
     * funcion para conectar el websocket
     */
    fun connect() {
        if (hasJoined) return
        hasJoined = true

        val request = Request.Builder()
            .url("$url$idTable")
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

    /**
     * funcion para recibir un mensaje y saaber que hacer con el
     * @param payload el mensaje que se ha enviado
     */
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
                val (name, tokens) = payload.content.split(":")

                val updatedPlayers = players.map {
                    if (it.name == name) it.copy(tokenAmount = tokens.toInt())
                    else it
                }
                players = updatedPlayers.toMutableList()

            }
            MessageType.SEND_PLAYER_DATA ->{
                val newListOfPlayers = Json.decodeFromString<List<PlayerDataToShow>>(payload.content)
                players = newListOfPlayers.toMutableList()
            }
            MessageType.SEND_ACTUAL_TABLE_AMOUNT ->{
                actualDeskAmount = payload.content
            }

            MessageType.END_ROUND -> {
                hasStartedRound = false
                showCards = false
                handRankingText = ""
                actualDeskAmount = ""

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

            else -> {}
        }

        val newListOfMsgs = messages + payload
        messages = newListOfMsgs.toMutableList()
    }

    /**
     * funcion para enviar que el cliente esta listo
     */
    fun sendReady() {
        sendMessage(Message(MessageType.PLAYER_READY, "true"))
    }

    /**
     * funcion para enviar una accion
     * @param action la accion que se va a realizar
     * @param amount la cantidad que se va a realizar la accion, por defecto esta en 0 porque no siempre vas a hacer raise
     */
    fun sendAction(action: BetAction, amount: Int = 0) {
        val payload = BetPayload(action, amount)
        sendMessage(Message(MessageType.ACTION, Json.encodeToString(payload)))
    }

    /**
     * fincion para enviar un mensaje al servidor
     * @param message el mensaje que se quiere enviar
     */
    fun sendMessage(message: Message) {
        webSocket?.send(Json.encodeToString(message))
    }

    /**
     * funcion para ddesconectarte del la mesa
     */
    fun disconnect() {
        webSocket?.close(4001, "")
    }
}
