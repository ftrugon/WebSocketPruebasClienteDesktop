package gameScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gameScreen.wsComm.Card
import java.util.*

/**
 * funcion para dibujar cartas de un jugador en el chat
 * @param player el jugador
 * @param cardsToDraw cartas del jugador
 */
@Composable
fun DrawCardsOnChat(player: String, cardsToDraw: List<Card>) {
    Box(
        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color(0xFFDDEEFF),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Cards of $player: ",
                color = Color.Black,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                cardsToDraw.forEach { card ->
                    DrawCard(card = card, cardWith = 30, cardHeight = 50)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val timestamp = String.format("%02d:%02d", hour, minute)

            Text(
                text = timestamp,
                fontSize = 12.sp,
                color = Color.DarkGray,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 4.dp)
            )
        }
    }
}