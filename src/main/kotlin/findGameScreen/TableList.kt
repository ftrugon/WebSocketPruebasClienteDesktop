package findGameScreen

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.model.Table
import gameScreen.GameScreen
import gameScreen.wsComm.PlayerInfoMessage
import mainMenuScreen.ButtonAlgo


@Composable
fun TableList(mesas: List<Table>) {

    val navigator = LocalNavigator.currentOrThrow

    var showFiltered by remember { mutableStateOf(false) }
    var selectedTable:Table? by remember { mutableStateOf(null) }

    var showCreateTableDialog by remember { mutableStateOf(false) }
    var stringFilter by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            //.background(Color.White),
            //.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        )
        {
            Box(modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center) {
                OutlinedTextField(
                    value = stringFilter,
                    onValueChange = { stringFilter = it },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.Gray
                        )
                    },
                    label = { Text("Search by title") },
                    singleLine = true,
                    modifier = Modifier,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        cursorColor = Color.White,
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.Gray,
                        leadingIconColor = Color.Gray
                    )
                )
            }

        }

        val filteredTables = if (showFiltered) {
            mesas.filter { it.numPlayers < 6 && it.bigBlind <= ApiData.userData!!.tokens }
        } else if (stringFilter.isNotEmpty()){
            mesas.filter { it.title.lowercase().contains(stringFilter.lowercase()) }
        } else {
            mesas
        }

        val listState = rememberLazyListState()

        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                state = listState
            ) {
                items(filteredTables) { mesa ->
                    TableCard(mesa){
                        selectedTable = mesa
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            VerticalScrollbar(modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState = listState))
        }


        ButtonAlgo(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), "Create Table"){
            showCreateTableDialog = true
        }


        if (showCreateTableDialog) {
            CreateTableDialog({showCreateTableDialog = false}){
                selectedTable = it
            }
        }

        if (selectedTable != null) {
            JoinTableDialog(selectedTable!!,
                {
                    navigator.push(GameScreen(selectedTable?._id ?: "",
                        it,
                        selectedTable?.title ?: "")
                    )
                } ,{selectedTable = null})
        }
    }
}