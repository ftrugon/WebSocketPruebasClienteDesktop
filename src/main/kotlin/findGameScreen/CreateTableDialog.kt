package findGameScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.navigator.Navigator
import data.dto.InsertTableDTO
import data.model.Table
import gameScreen.GameScreen
import gameScreen.wsComm.PlayerInfoMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mainMenuScreen.ButtonAlgo


@Composable
fun CreateTableDialog(
    onDismissRequest: () -> Unit,
    onTableCreated: (Table) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var bigBlind by remember { mutableStateOf("") }

    var errorText by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            //color = MaterialTheme.colors.surface,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Crear mesa", style = MaterialTheme.typography.h6)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") }
                )

                OutlinedTextField(
                    value = bigBlind,
                    onValueChange = { bigBlind = it.filter { tal -> tal.isDigit() } },
                    label = { Text("Big Blind") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                if (errorText.isNotEmpty()) {
                    Text(text = errorText, color = Color.Red)
                }

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancelar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    var isLoading by remember { mutableStateOf(false) }

                    ButtonAlgo(
                        modifier = Modifier,
                        text = "Create table",
                        enabled = !isLoading
                    ) {
                        if (title.isBlank() || description.isBlank() || bigBlind.isBlank()) {
                            errorText = "All fields are required"
                        }

                        if (errorText.isEmpty()) {
                            val dto = InsertTableDTO(title, description, bigBlind.toIntOrNull()?: 2)

                            errorText = ""

                            CoroutineScope(Dispatchers.IO).launch {
                                isLoading = true
                                val success = ApiData.insertTable(dto).await()
                                isLoading = false

                                if (success != null) {
                                    onTableCreated(success)
                                }else {
                                    errorText = "Error al obtener los datos"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}