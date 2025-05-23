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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.navigator.Navigator
import data.model.Table
import gameScreen.App
import gameScreen.GameScreen
import gameScreen.PlayerInfoMessage


@Composable
fun JoinTableDialog(
    mesa: Table,
    navigator: Navigator,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
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
                    color = Color.White
                )
                Text(
                    text = "${mesa.title} -> ${mesa.numPlayers}/6",
                    style = MaterialTheme.typography.body2,
                    color = Color.White
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
                            navigator.push(GameScreen(mesa._id,
                                PlayerInfoMessage(ApiData.userData?.username?:"",dineroSeleccionado.toInt()),mesa.title,))
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