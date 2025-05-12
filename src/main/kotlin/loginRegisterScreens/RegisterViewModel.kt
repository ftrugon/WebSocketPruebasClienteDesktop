package loginRegisterScreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class RegisterViewModel {

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var repeatPassword by mutableStateOf("")

    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onRepeatPasswordChange(newPassword: String) {
        repeatPassword = newPassword
    }

}