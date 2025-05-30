package gameScreen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import gameScreen.wsComm.Card
import kotlin.math.roundToInt


@Composable
fun DrawCommCards(
    modifier: Modifier = Modifier,
    cardList: List<Card>,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 800, easing = FastOutSlowInEasing),
    fromBottom: Boolean = true,
    visible: Boolean = true
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val density = LocalDensity.current
        val offsetY = remember { Animatable(0f) }

        LaunchedEffect(cardList, visible) {
            val offScreenOffset = with(density) {
                if (fromBottom) maxHeight.toPx() else -maxHeight.toPx()
            }

            if (visible) {
                // Aparece: animar desde fuera hacia 0
                offsetY.snapTo(offScreenOffset)
                offsetY.animateTo(0f, animationSpec)
            } else {
                // Desaparece: animar desde 0 hacia fuera
                offsetY.snapTo(0f)
                offsetY.animateTo(offScreenOffset, animationSpec)
            }
        }

        Row(
            modifier = Modifier
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            for (card in cardList) {
                DrawCard(card,80,120)
            }
        }
    }
}