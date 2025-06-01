package userProfileScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import loginRegisterScreens.LoginScreen
import mainMenuScreen.ButtonAlgo


@Composable
fun UserProfileContent() {
    val usuario = ApiData.userData
    val navigator = LocalNavigator.currentOrThrow
    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf("") }
    var inputText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Perfil a la izquierda
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color(0xAA000000), shape = RoundedCornerShape(12.dp))
                .padding(24.dp)
                .fillMaxWidth(0.6f)
        ) {
            Text("User Profile", style = MaterialTheme.typography.h4.copy(color = Color.White))
            Spacer(Modifier.height(16.dp))
            ProfileItem("ID", usuario?._id ?: "N/A")
            Spacer(Modifier.height(8.dp))
            ProfileItem("Username", usuario?.username ?: "N/A")
            Spacer(Modifier.height(8.dp))
            ProfileItem("Tokens", usuario?.tokens.toString())
            Spacer(Modifier.height(8.dp))
            ProfileItem("Role", usuario?.roles ?: "USER")
        }

        // Botones inferiores
        Row(
            modifier = Modifier.align(Alignment.BottomStart),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(32.dp)
            //verticalArrangement = Arrangement.spacedBy(12.dp),
            //horizontalAlignment = Alignment.End
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ButtonAlgo(Modifier,"Insert tokens"){
                    dialogType = "insert"
                    inputText = ""
                    showDialog = true
                }
                ButtonAlgo(Modifier,"Retire tokens"){
                    dialogType = "retire"
                    inputText = ""
                    showDialog = true
                }
            }

            ButtonAlgo(Modifier,"Change username"){
                dialogType = "username"
                inputText = ""
                showDialog = true
            }

        }

        Button(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = { navigator.push(LoginScreen()) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
        ) {
            Text("Log out!", color = Color.White)
        }

        // Diálogo simple para entrada
        if (showDialog) {
            DialogOfRetireInsertAndChangeUsername(
                onDismiss = {showDialog = false},
                dialogType =  dialogType,
                inputText = inputText,
                onInputValueChange = {inputText = it},
                onConfirm = {
                    showDialog = false
                    scope.launch {
                        when (dialogType) {
                            "insert" -> ApiData.insertTokens(inputText.toIntOrNull() ?: 0)
                            "retire" -> ApiData.retireTokens(inputText.toIntOrNull() ?: 0)
                            "username" -> ApiData.changeUsername(inputText)
                        }
                    }
                },
                onCancel = {
                    showDialog = false
                })

        }

    }
}
