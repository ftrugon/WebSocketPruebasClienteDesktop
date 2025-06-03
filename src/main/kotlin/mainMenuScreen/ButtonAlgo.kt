package mainMenuScreen

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * funcion para dibujaar un boton con el tema de la aplicacion
 * @param modifier el modifgicador del boton
 * @param text el texto que tenga el boton
 * @param enabled si el boton esta o no activo
 * @param onClick lo que se produce al darle al boton
 */
@Composable
fun ButtonAlgo(
    modifier: Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
){
    OutlinedButton(
        onClick = { onClick() },
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = Color.Gray.copy(alpha = 0.40f),
            contentColor = Color.White
        ),
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}