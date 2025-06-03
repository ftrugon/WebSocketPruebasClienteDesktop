package findGameScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.navigator.Navigator
import data.model.Table
import gameScreen.GameScreen
import gameScreen.wsComm.PlayerInfoMessage

/**
 * dialog para unirte a una partida
 * @param mesa la mesa a la que te vas a unir
 * @param onJoin funcionq ue ocurre aal boton de join
 * @param onDismiss funcion que ocurre cuando  cancelas el dialog
 */
@Composable
fun JoinTableDialog(
    mesa: Table,
    //navigator: Navigator,
    onJoin:(PlayerInfoMessage) -> Unit,
    onDismiss: () -> Unit
) {

    var tryJoin by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {

        Surface(
            shape = RoundedCornerShape(12.dp),
            //color = MaterialTheme.colors.surface,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF1A1A1A),
                        shape = RoundedCornerShape(16.dp)
                    )
            ){
                var dineroSeleccionado by remember { mutableStateOf(mesa.bigBlind.toFloat()) }

                Column(
                    modifier = Modifier
                        .padding(24.dp)
                )
                {
                    Text(
                        text = "Do you want to join this table?",
                        style = MaterialTheme.typography.h3,
                        color = Color.White,
                        maxLines = 1
                    )
                    Text(
                        text = "${mesa.title} -> ${mesa.numPlayers}/6",
                        style = MaterialTheme.typography.body2,
                        color = Color.White,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Cantidad: ${dineroSeleccionado.toInt()} tokens")

                        Slider(
                            value = dineroSeleccionado,
                            onValueChange = {
                                dineroSeleccionado = it
                            },
                            valueRange = mesa.bigBlind.toFloat()..ApiData.userData!!.tokens.toFloat(),
                            steps = (ApiData.userData!!.tokens - mesa.bigBlind).coerceAtLeast(0)
                        )

                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel", color = Color.LightGray)
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        val canJoin = mesa.numPlayers < 6

                        TextButton(
                            onClick = {
                                if (!tryJoin) {
                                    tryJoin = true
                                    onJoin(
                                        PlayerInfoMessage(
                                            ApiData.userData?.username ?: "",
                                            dineroSeleccionado.toInt()
                                        )
                                    )

                                }
                            },
                            enabled = canJoin,
                        ) {
                            Text("Join", color = Color.White)
                        }
                    }
                }
            }



        }

    }
}