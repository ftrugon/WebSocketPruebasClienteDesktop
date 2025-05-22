package loginRegisterScreens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation


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
