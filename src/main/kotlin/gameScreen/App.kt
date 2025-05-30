package gameScreen

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gameScreen.wsComm.BetAction
import gameScreen.wsComm.BetPayload
import gameScreen.wsComm.Message
import gameScreen.wsComm.MessageType
import gameScreen.wsComm.PlayerInfoMessage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun App(idTable: String, playerInfo: PlayerInfoMessage, tableTitle: String) {

    val navigator = LocalNavigator.currentOrThrow
    val listState = rememberLazyListState()

    // iniciando el viewmodel
    val viewModel = remember {
        PokerViewModel(idTable, playerInfo) {
            navigator.pop()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.connect()
    }

    // Contenido de las cosas
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

            // parte que enseña las cosas
            BoxWithConstraints(
                modifier = Modifier.weight(4f).fillMaxSize(),
                contentAlignment = Alignment.Center
            )
            {

                Text(
                    modifier = Modifier.align(Alignment.TopCenter),
                    text = if (viewModel.isConnected) "Connected to table: $tableTitle" else "Disconnected from table: $tableTitle",
                    color = if (viewModel.isConnected) Color(0xFF81C784) else Color(0xFFE57373),
                )

                Spacer(Modifier.height(8.dp))

                if (viewModel.hasStartedRound)
                {
                    DrawCommCards(Modifier
                        .fillMaxWidth(),viewModel.commCards, fromBottom = false, visible = viewModel.showCards)

                    DrawPlayersAroundCircle(playerInfo.name,viewModel.userCards,viewModel.players)
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(8.dp)
                    )
                    {
                        Text(viewModel.handRankingText)
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        )
                        {

                            Button(
                                onClick = {
                                    viewModel.sendAction(BetAction.RAISE,10)

                                },
                                enabled = viewModel.isConnected,
                                modifier = Modifier.padding(horizontal = 10.dp)) {
                                Text("Raise 10")
                            }

                            Button(onClick = {
                                viewModel.sendAction(BetAction.CALL)
                            },
                                enabled = viewModel.isConnected,
                                modifier = Modifier.padding(horizontal = 10.dp)) {
                                Text("Call / Check")
                            }

                            Button(onClick = {
                                viewModel.sendAction(BetAction.FOLD)
                            },
                                enabled = viewModel.isConnected,
                                modifier = Modifier.padding(horizontal = 10.dp)) {
                                Text("Fold")
                            }
                        }
                    }

                }else
                {

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

                    },
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
                    .weight(1f),

                contentAlignment = Alignment.Center
            )
            {
                SimpleChat(listState,viewModel.messages, { viewModel.sendMessage(it) },viewModel.isConnected,
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(Color.White.copy(alpha = 0.40f))
                )
            }

        }

        // boton de salir de abajo a la derecha
        ExitButton(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        ){
            viewModel.disconnect()
        }
    }


}

@Composable
fun ExitButton(
    modifier: Modifier,
    onClick: () -> Unit,
){
    Button(
        onClick = {
            onClick()
            //webSocket?.close(4001,"")
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFE57373),
            contentColor = Color.White
        ),
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.ExitToApp,
            contentDescription = "Salir",
            modifier = Modifier.size(20.dp)
        )
    }
}