package loginRegisterScreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * clase para el viewmoddel del logiin
 */
class LoginViewModel {

    var username by mutableStateOf("")
    var password by mutableStateOf("")


    /**
     * funcion para cambiar el usuario
     * @param newUsername el nuevo texto que va a tener el usuario como username
     */
    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    /**
     * funcion para cambiar de combtraseña
     * @param newPassword el texto que va a tener como contraseña
     */
    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

}