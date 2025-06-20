package userProfileScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

/**
 * cajita con el contenido de la informacion de usuario, sirve para el  userrname, los tokens
 * @param label el lable del texto
 * @param value el valor del texto
 */
@Composable
fun ProfileItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.caption.copy(color = Color.LightGray)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}