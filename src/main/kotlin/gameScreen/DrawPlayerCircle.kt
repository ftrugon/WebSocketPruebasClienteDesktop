package gameScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/**
 * funcoin para dibujar el usuario y sus tokens
 */
@Composable
fun DrawPlayerCircle(player: String,tokensAmount: Int) {
    // Círculo con nombre
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(Color.DarkGray, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = player,
            color = Color.White,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            modifier = Modifier.align(Alignment.TopCenter),
            text = tokensAmount.toString(),
            color = Color.White,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}