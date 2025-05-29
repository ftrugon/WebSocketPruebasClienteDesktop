package findGameScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import data.model.Table


@Composable
fun TableCard(mesa: Table, onClickTable: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()

            .background(
                color = Color.LightGray.copy(alpha = 0.40f),
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
                    color = Color.Black,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = mesa.desc,
                style = MaterialTheme.typography.body1.copy(color = Color.DarkGray),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Active players: ${mesa.numPlayers}/6",
                    style = MaterialTheme.typography.body2.copy(color = Color.DarkGray),
                )
                Text(
                    text = "Big blind: ${mesa.bigBlind}",
                    style = MaterialTheme.typography.body2.copy(color = Color.DarkGray),
                )
            }
        }
    }
}