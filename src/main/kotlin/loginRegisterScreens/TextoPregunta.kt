package loginRegisterScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

/**
 * texto de abajo de los de inicio de sesion y registro
 * @param textoUno el texto que hace como pregunta
 * @param textoDos el texto que se puede clicar
 * @param onClick la funcion que se hace al darke al texto clicable
 */
@Composable
fun TextoPregunta(
    textoUno: String,
    textoDos: String,
    onClick: () -> Unit
) {
    Text(
        buildAnnotatedString {
            append(textoUno)
            withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
                append(textoDos)
            }
        },
        modifier = Modifier
            .clickable {
                // Aquí puedes manejar la navegación al registro
                onClick()
            }
            .padding(top = 8.dp),
        textAlign = TextAlign.Center
    )
}