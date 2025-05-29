package userProfileScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.model.Usuario
import loginRegisterScreens.LoginScreen
import mainMenuScreen.ActualScreen
import mainMenuScreen.ColumnaIzquierda
import mainMenuScreen.MainMenuScreen

class UserProfileScreen : Screen {
    @Composable
    override fun Content() {
        ColumnaIzquierda("background_image_login.png", ActualScreen.USER_PROFILE){
            UserProfileContent()
        }
    }
}


@Composable
fun UserProfileContent() {

    val usuario = ApiData.userData
    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Perfil de usuario", style = MaterialTheme.typography.h5)

        ProfileItem(label = "ID", value = usuario?._id ?: "N/A")
        ProfileItem(label = "Nombre de usuario", value = usuario?.username ?: "N/A")
        ProfileItem(label = "Tokens", value = usuario?.tokens.toString())
        ProfileItem(label = "Rol", value = usuario?.roles ?: "USER")

        Spacer(Modifier.height(32.dp))

        Button(onClick = {
            navigator.push(LoginScreen())
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)) {
            Text("Cerrar sesión", color = Color.White)
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.caption)
        Text(text = value, style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold))
    }
}