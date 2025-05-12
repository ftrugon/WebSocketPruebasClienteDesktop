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
import kotlinx.coroutines.launch

class LoginScreen : Screen {

    // val loginViewModel = sera el viewmodel del login

    @Composable
    override fun Content() {
        LogoIzqFormDcha("background_image_login.png"){
            Login()
        }
    }

}

@Composable
fun Login() {

    val navigator = LocalNavigator.currentOrThrow
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf("") }
    var isCharging by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()


    if (isCharging) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }

    Text("Log in!", style = MaterialTheme.typography.h5)

    Spacer(modifier = Modifier.height(16.dp))

    CampoAlgo("Username", username,{username = it},false)

    CampoAlgo("Password",password,{password = it}, true)

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick ={

            coroutineScope.launch{

                isCharging = true
                val (token, success) = ApiData.getToken(username, password).await()
                isCharging = false
                if (success) {
                    println("Successfully logged in!")
                } else {
                    if (token.contains("401")){
                        errorMessage = "Login incorrecto"
                    }else{
                        errorMessage = "Error en la conexion"
                    }

                    showError = true
                }
            }

        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Enter!")
    }

    Spacer(modifier = Modifier.height(8.dp))

    TextoPregunta("Not registered yet?","Register Here!"){
        navigator.push(RegisterScreen())
    }

    if (showError) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(errorMessage, color = Color.Red)
    }

}

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

@Composable
fun CampoAlgo(
    label: String,
    campo: String,
    onChangeCampo: (String) -> Unit,
    esContrasena: Boolean,
){


    var showPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = campo,
        onValueChange = { onChangeCampo(it) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (showPassword) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = { IconButton({ showPassword = !showPassword }) {

                if (esContrasena) {
                    Icon(Icons.Filled.Create
                        , contentDescription = "Password")
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun LogoIzqFormDcha(
    imgFondo: String,
    content: @Composable () -> Unit
){
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

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // LOGO al centro del eje vertical
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(4f),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource("icon_image.png"),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .scale(0.30f)
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, Color.LightGray, RoundedCornerShape(16.dp))

                )
            }

            // FORMULARIO pegado a la derecha
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    //.width(350.dp)
                    .weight(2f),
                //  .padding(24.dp)

                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.40f))
                        .padding(24.dp)
                        .fillMaxSize()
                ) {
                    content()
                }

            }
        }
    }
}