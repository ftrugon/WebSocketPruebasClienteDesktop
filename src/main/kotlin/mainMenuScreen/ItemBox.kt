package mainMenuScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.model.BetDocument

/**
 * caaja que aalmacena el registro de unaa apuesta para dbujarlo
 * @param bet la apuesta a dibujar
 */
@Composable
fun ItemBox(bet: BetDocument) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .background(Color.White.copy(alpha = 0.40f))
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = "Amount: ${bet.amount}",
                style = MaterialTheme.typography.body2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Type: ${bet.type}",
                style = MaterialTheme.typography.body2,
                color = Color.DarkGray
            )
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}
