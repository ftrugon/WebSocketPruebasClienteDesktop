import androidx.compose.material.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import cafe.adriel.voyager.transitions.SlideTransition
import welcomeScreen.WelcomeScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Casino 404",
        //resizable = false,
        // icon = ,
        state = rememberWindowState(width = 1000.dp, height = 700.dp),
    ) {

        MaterialTheme {
            Navigator(WelcomeScreen()){
                SlideTransition(it)
            }
        }

    }
}

