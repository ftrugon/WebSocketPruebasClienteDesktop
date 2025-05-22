package loginRegisterScreens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import mainMenuScreen.MainMenuScreen


@Composable
fun Login() {

    val navigator = LocalNavigator.currentOrThrow
    val viewModel = remember { LoginViewModel() }
    val coroutineScope = rememberCoroutineScope()

//    var username by remember { mutableStateOf(viewModel.username) }
//    var password by remember { mutableStateOf(viewModel.password) }

    //var username by remember { mutableStateOf("") }
    //var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isCharging by remember { mutableStateOf(false) }



    if (isCharging) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }

    Text("Log in!", style = MaterialTheme.typography.h5)

    Spacer(modifier = Modifier.height(16.dp))

    CampoAlgo("Username", viewModel.username,{viewModel.onUsernameChange(it)},false)

    CampoAlgo("Password",viewModel.password,{ viewModel.onPasswordChange(it)}, true)

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick ={

            coroutineScope.launch{

                isCharging = true
                val (token, success) = ApiData.getToken(viewModel.username, viewModel.password).await()
                isCharging = false
                if (success) {
                    navigator.push(MainMenuScreen())
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


