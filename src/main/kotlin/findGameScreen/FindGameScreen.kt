package findGameScreen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import data.model.Table
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(mesas) { mesa ->
            TableCard(mesa)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun TableCard(mesa: Table) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF1A1A1A).copy(alpha = 0.6f), // fondo oscuro semitransparente
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
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
                    text = "Jugadores activos: ${mesa.numPlayers}",
                    style = MaterialTheme.typography.body2.copy(color = Color.Gray)
                )
                Text(
                    text = "Ciega grande: ${mesa.bigBlind}",
                    style = MaterialTheme.typography.body2.copy(color = Color.Gray)
                )
            }
        }
    }
}
