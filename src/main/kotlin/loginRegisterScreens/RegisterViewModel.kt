package loginRegisterScreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * El viewmodel para el registro
 */
class RegisterViewModel {

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var repeatPassword by mutableStateOf("")

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

    /**
     * funcion para cambiar de repetir combtraseña
     * @param newPassword el texto que va a tener como repetir contraseña
     */
    fun onRepeatPasswordChange(newPassword: String) {
        repeatPassword = newPassword
    }

}