package userProfileScreen

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable


@Composable
fun DialogOfRetireInsertAndChangeUsername(
    onDismiss: () -> Unit,
    dialogType: String,
    inputText: String,
    onInputValueChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
){
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                when (dialogType) {
                    "insert" -> "Insert tokens"
                    "retire" -> "Retire tokens"
                    "username" -> "Change username"
                    else -> ""
                }
            )
        },
        text = {
            OutlinedTextField(
                value = inputText,
                onValueChange = { onInputValueChange(it) },
                label = {
                    Text(
                        when (dialogType) {
                            "insert", "retire" -> "Amount"
                            "username" -> "New username"
                            else -> ""
                        }
                    )
                },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = {
                onConfirm()
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onCancel() }) {
                Text("Cancel")
            }
        }
    )
}