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
import data.dto.RegistrarUsuarioDTO
import kotlinx.coroutines.launch

/**
 * funcion con el conteniuddo del registro
 */
@Composable
fun Register() {


    val coroutineScope = rememberCoroutineScope()
    val viewModel = remember { RegisterViewModel() }
    val navigator = LocalNavigator.currentOrThrow

    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isCharging by remember { mutableStateOf(false) }


    if (isCharging) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    }

    Text("Register!", style = MaterialTheme.typography.h5)

    Spacer(modifier = Modifier.height(16.dp))

    CampoAlgo(
        "Username",
        viewModel.username,
        {viewModel.onUsernameChange(it)},
        false)

    Spacer(modifier = Modifier.height(12.dp))

    CampoAlgo(
        "Password",
        viewModel.password,
        {viewModel.onPasswordChange(it)},
        true)

    CampoAlgo(
        "Repeat Password",
        viewModel.repeatPassword,
        {viewModel.onRepeatPasswordChange(it)},
        true)

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick ={

            coroutineScope.launch{

                val userToAd = RegistrarUsuarioDTO(viewModel.username,viewModel.password,viewModel.repeatPassword)

                isCharging = true

                val (token, success) = ApiData.registrarUsuario(userToAd).await()
                isCharging = false
                if (success) {
                    navigator.push(LoginScreen())
                } else {
                    if (token.contains("401")){
                        errorMessage = "Registration Failed!: $token"
                    }else{
                        errorMessage = token
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
