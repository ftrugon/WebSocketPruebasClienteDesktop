package gameScreen

import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


/**
 * boton para salir de la mesa abajo a la derecha dde la screen
 * @param modifier el moficador del boton
 * @param onClick la funcion que se realiza al clicar el boton
 */
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