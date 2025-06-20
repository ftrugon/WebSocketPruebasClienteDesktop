package gameScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gameScreen.wsComm.Card
import gameScreen.wsComm.CardSuit
import gameScreen.wsComm.CardValue
import gameScreen.wsComm.PlayerDataToShow
import kotlin.math.cos
import kotlin.math.sin


/**
 * funcion para ddibujar los jugadores como su fiera uin circulo con las cartas abajo
 */
@Composable
fun DrawPlayersAroundCircle(
    playerName: String,
    userCards: List<Card>,
    players: List<PlayerDataToShow>,
    radius: Dp = 350.dp
) {

    BoxWithConstraints(
        Modifier.fillMaxSize().offset(y = (-70).dp),
    ) {
        val density = LocalDensity.current
        val centerX = constraints.maxWidth / 2
        val centerY = constraints.maxHeight / 2
        val radiusPx = with(density) { radius.toPx() }

        val angleStep = 360f / players.size

        players.forEachIndexed { index, (player,tokensAmount) ->
            val angleRad = Math.toRadians((angleStep * index - 90).toDouble())
            val x = centerX + (radiusPx * cos(angleRad)).toInt()
            val y = (centerY + (radiusPx * sin(angleRad)).toInt() / 1.55).toInt()

            Column(
                modifier = Modifier
                    .offset {
                        IntOffset(x - 86, y - 40)
                    }
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Círculo con nombre y tokens
                DrawPlayerCircle(player,tokensAmount)

                // Cartas solo si es el jugador actual
                Spacer(modifier = Modifier.height(8.dp))

                // las cartas de cada jugador

                Column(
                    modifier = Modifier
                        .width((userCards.size * 88).dp)
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //contentAlignment = Alignment.Center
                ) {
                    Row {
                        if (player == playerName) {
                            userCards.forEach { card ->
                                DrawCard(card,60,90)
                            }
                        }else {
                            repeat(2) {
                                DrawCard(Card(CardSuit.NONE, CardValue.NONE), 60, 90)
                            }
                        }
                    }
                }

            }
        }
    }
}


