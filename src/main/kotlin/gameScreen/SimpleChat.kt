package gameScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import gameScreen.wsComm.Message
import gameScreen.wsComm.MessageType
import gameScreen.wsComm.PlayerDataToShow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.WebSocket

/**
 * funcion para ddiibujar el chat de la derecha en meddio dde la partida
 * @param listState el state de la lista, para que scrolleee automaticamente hacia abajo
 * @param messages la lista de mensajes que tiene que escribir
 * @param onSendMessage mandar el mensaje
 * @param isConnected si esta o no conectado
 * @param modifier el modifier de la columna que contiene esto
 */
@Composable
fun SimpleChat(
    listState: LazyListState,
    messages: List<Message>,
    onSendMessage: (Message) -> Unit,
    //webSocket: WebSocket?,
    isConnected: Boolean,
    modifier: Modifier = Modifier
) {

    // meter la parte de escribir en el viewmodel
    var inputMessage by remember { mutableStateOf("") }

    Column(modifier = modifier) {
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
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
                        val player = Json.decodeFromString<PlayerDataToShow>(it.content)
                        Text("${player.name} has joined the table.")
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
                        val name = it.content.split(":").first()
                        Text("Turn of $name")
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
                    onSendMessage(msg)
                    //webSocket?.send(Json.encodeToString(msg))
                    inputMessage = ""
                },enabled = isConnected) {
                    Icon(Icons.Default.Send, contentDescription = "")
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (isConnected && inputMessage.isNotBlank()) {
                        val msg = Message(MessageType.TEXT_MESSAGE, inputMessage)
                        onSendMessage(msg)
                        inputMessage = ""
                    }
                }
            ),
        )

    }
}