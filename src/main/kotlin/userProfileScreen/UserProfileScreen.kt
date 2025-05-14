package userProfileScreen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import mainMenuScreen.ActualScreen
import mainMenuScreen.ColumnaIzquierda

class UserProfileScreen : Screen {
    @Composable
    override fun Content() {
        ColumnaIzquierda("background_image_login.png", ActualScreen.USER_PROFILE){
            Text("hola")
        }
    }
}