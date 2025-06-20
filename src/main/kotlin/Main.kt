import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import cafe.adriel.voyager.transitions.SlideTransition
import gameScreen.App
import theme.CasinoTheme
import welcomeScreen.WelcomeScreen

/**
 * funcion principal del programa, contiene el application y ddentro el composable con la app
 */
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Casino 404",
        //resizable = false,
        icon = painterResource("icon_image.png"),
        state = rememberWindowState(width = 1100.dp, height = 900.dp)
    ) {

        //App()
        CasinoTheme {
            Navigator(WelcomeScreen()){
                SlideTransition(it)
            }
        }

    }
}

