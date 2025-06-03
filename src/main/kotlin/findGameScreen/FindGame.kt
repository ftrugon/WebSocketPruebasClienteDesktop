package findGameScreen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import data.model.Table

/**
 * funciom que contiene las partes para la screen de buscar una mesa
 */
@Preview()
@Composable
fun FindGame(){

    var allTables by remember { mutableStateOf(listOf<Table>()) }

    LaunchedEffect(Unit) {

        allTables = ApiData.getALlTables().await()

    }

    TableList(allTables)

}