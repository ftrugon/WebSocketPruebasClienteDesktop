package findGameScreen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import data.model.Table
import loginRegisterScreens.CampoAlgo
import mainMenuScreen.ActualScreen
import mainMenuScreen.ColumnaIzquierda

class FindGameScreen: Screen {

    @Composable
    override fun Content() {
        ColumnaIzquierda("background_image_login.png", ActualScreen.FIND_GAME){
            findGame()
        }
    }

}


@Preview()
@Composable
fun findGame(){


    // Pillar de la base de datos
    val mesas = mutableListOf<Table>(
        Table("","Mesa españoles por aquí", "Mesa de 5 españoles, nos falta 1", 5, 5),
        Table("","High Rollers", "Only high-stakes players, join if you dare", 3, 100),
        Table("","Casual vibes", "Chill table, just here for fun", 4, 10),
        Table("","Noche de póker", "Partida amistosa entre colegas", 6, 20),
        Table("","The Bluff Masters", "Expert bluffers welcome", 2, 50),
        Table("","Mesa latina", "Jugadores de toda Latinoamérica", 4, 25),
        Table("","Gringos welcome", "Open table for anyone, beginners too", 1, 5),
        Table("","Mesa full española", "Solo españoles, buen rollo", 6, 10),
        Table("","Noobs & Pros", "Mixta, novatos y expertos", 3, 15),
        Table("","All In Fiesta", "Aquí venimos a arriesgar", 5, 50),
        Table("","Late Night Poker", "Partida nocturna, ritmo tranquilo", 4, 20),
        Table("","Let's gamble", "We don’t fold here", 5, 100),
        Table("","Poker de barrio", "Los de siempre, pero con cartas", 6, 10),
        Table("","Grind Session", "Serious play, no chatting", 2, 200),
        Table("","Mesa de paso", "Para partidas rápidas", 3, 10),
        Table("","Friendly Fire", "Jugamos duro pero con respeto", 4, 25),
        Table("","All welcome", "International table, English only", 5, 20),
        Table("","Póker en pijama", "Para los que juegan desde la cama", 6, 5),
        Table("","Crazy Blinds", "Suben cada ronda", 2, 150),
        Table("","Mesa sin miedo", "Aquí nadie se rinde", 3, 30)
    )


    TableListScreen(mesas)

}

@Composable
fun TableListScreen(mesas: List<Table>) {

    var showFiltered by remember { mutableStateOf(false) }
    var selectedTable:Table? by remember { mutableStateOf(null) }

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

            Box(modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center) {
                Button(
                    onClick = { showFiltered = !showFiltered },
                    colors = ButtonDefaults.buttonColors(Color.Gray)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Show only joinable tables",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (showFiltered) "All tables" else "Only joinable tables",
                        color = Color.White
                    )
                }
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


        Button(modifier = Modifier.fillMaxWidth(), onClick = {

        }){
            Text("Create Table")
        }

        if (selectedTable != null) {
            JoinTableDialog(selectedTable!!,{
                // ir y conectarte a la mesa
            },{selectedTable = null})
        }
    }
}

@Composable
fun JoinTableDialog(
    mesa: Table,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF1A1A1A),
                    shape = RoundedCornerShape(16.dp)
                )
        ){
            Column(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                Text(
                    text = "Do you want to join this table?",
                    style = MaterialTheme.typography.h3,
                    color = Color.White
                )
                Text(
                    text = "${mesa.title} -> ${mesa.numPlayers}/6",
                    style = MaterialTheme.typography.body2,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = Color.LightGray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    val canJoin = if (mesa.numPlayers < 6) true else true

                    TextButton(
                        onClick = onConfirm,
                        enabled = canJoin,
                    ) {

                        Text("Join", color = Color.White)
                    }
                }
            }
        }


    }
}



@Composable
fun TableCard(mesa: Table, onClickTable: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF1A1A1A).copy(alpha = 0.6f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .clickable {
                onClickTable()
            }
    ) {
        Column {
            Text(
                text = mesa.title,
                style = MaterialTheme.typography.h3.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = mesa.desc,
                style = MaterialTheme.typography.body1.copy(color = Color(0xFFDDDDDD))
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Active players: ${mesa.numPlayers}/6",
                    style = MaterialTheme.typography.body2.copy(color = Color.Gray)
                )
                Text(
                    text = "Big blind: ${mesa.bigBlind}",
                    style = MaterialTheme.typography.body2.copy(color = Color.Gray)
                )
            }
        }
    }
}
