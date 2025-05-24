package gameScreen

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import welcomeScreen.WelcomeScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Casino 404",
        //resizable = false,
        icon = painterResource("icon_image.png"),
        state = rememberWindowState(width = 1000.dp, height = 700.dp),
    ) {

        MaterialTheme {
            Navigator(GameScreen("sdasd", PlayerInfoMessage("",123),"mesa de prueb jasjajsajsjasasasasasasa")){
                SlideTransition(it)
            }
        }


    }
}