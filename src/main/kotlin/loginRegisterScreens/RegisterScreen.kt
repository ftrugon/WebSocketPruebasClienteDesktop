package loginRegisterScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.dto.RegistrarUsuarioDTO
import kotlinx.coroutines.launch

class RegisterScreen : Screen {
    @Composable
    override fun Content() {
        LogoIzqFormDcha("background_image_register.png"){
            Register()
        }
    }
}


@Composable
fun Register() {

    val navigator = LocalNavigator.currentOrThrow
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    var isCharging by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (isCharging) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }

    Text("Register!", style = MaterialTheme.typography.h5)

    Spacer(modifier = Modifier.height(16.dp))

    CampoAlgo("Username", username,{username = it},false)

    Spacer(modifier = Modifier.height(12.dp))

    CampoAlgo("Password",password,{password = it}, true)

    CampoAlgo("Repeat Password",repeatPassword,{repeatPassword = it}, true)

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick ={

            coroutineScope.launch{

                val userToAd = RegistrarUsuarioDTO(username,password,repeatPassword)

                isCharging = true
                val (token, success) = ApiData.registrarUsuario(userToAd).await()
                isCharging = false
                if (success) {
                    navigator.push(LoginScreen())
                } else {
                    if (token.contains("401")){
                        errorMessage = "Registration Failed!"
                    }else{
                        errorMessage = "Error en la conexion"
                    }

                    showError = true
                }
            }

        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Register")
    }

    Spacer(modifier = Modifier.height(8.dp))

    TextoPregunta("Have Account?","Log in Here!"){
        navigator.push(LoginScreen())
    }

    if (showError) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(errorMessage, color = Color.Red)
    }

}
