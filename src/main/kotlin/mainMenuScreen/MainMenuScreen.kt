package mainMenuScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import findGameScreen.FindGameScreen
import userProfileScreen.UserProfileScreen

class MainMenuScreen : Screen {
    @Composable
    override fun Content() {
        ColumnaIzquierda("background_image_login.png", ActualScreen.MAIN_MENU){
            MainMenu()
        }
    }
}

enum class ActualScreen{
    MAIN_MENU, FIND_GAME,USER_PROFILE
}

@Composable
fun ColumnaIzquierda(
    imgFondo: String,
    actualScreen: ActualScreen,
    content: @Composable () -> Unit
) {

    val navigator = LocalNavigator.currentOrThrow

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(imgFondo),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Row(Modifier.fillMaxSize()) {
            // Sidebar
            Column(
                modifier = Modifier
                    .width(72.dp)
                    .fillMaxHeight()
                    .background(Color.LightGray.copy(alpha = 0.40f)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {
                    if (actualScreen != ActualScreen.MAIN_MENU) {
                        navigator.push(MainMenuScreen())
                    }
                }) {
                    Icon(Icons.Default.Home, contentDescription = "Inicio")
                }
                IconButton(onClick = {
                    if (actualScreen != ActualScreen.FIND_GAME) {
                        navigator.push(FindGameScreen())
                    }

                }) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
                IconButton(onClick = {
                    if (actualScreen != ActualScreen.USER_PROFILE) {
                        navigator.push(UserProfileScreen())
                    }
                }) {
                    Icon(Icons.Default.Person, contentDescription = "Perfil")
                }
            }

            content()

        }
    }
}

@Composable
fun MainMenu(){


    // Main content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Fila 1
        Row(Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .background(Color.Gray.copy(alpha = 0.40f))
            ){
                Text("Profile picture")
            }
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .background(Color.Gray.copy(alpha = 0.40f))
            ){
                Text("Num of tokens")
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(4.dp)
                        .background(Color.DarkGray.copy(alpha = 0.40f))
                ){
                    Text("Add tokens")
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(4.dp)
                        .background(Color.DarkGray.copy(alpha = 0.40f))
                ){
                    Text("Retire tokens")
                }
            }
        }

        // Fila 2
        Row(modifier = Modifier
            .weight(1f)
            .fillMaxSize()
            .padding(4.dp)
            .background(Color.Gray.copy(alpha = 0.40f))) {
            Text("Si me da tiempo un historial de todas las apuiestas hechas en las mesas")

        }

        // Fila 3
        Row(Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .background(Color.Gray.copy(alpha = 0.40f))
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .background(Color.Gray.copy(alpha = 0.40f))
            )
        }
    }
}
