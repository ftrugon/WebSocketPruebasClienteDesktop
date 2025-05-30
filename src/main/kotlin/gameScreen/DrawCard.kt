package gameScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import gameScreen.wsComm.Card
import gameScreen.wsComm.CardSuit
import gameScreen.wsComm.CardValue


@Composable
fun DrawCard(card: Card, cardWith: Int, cardHeight: Int) {

    var imageUrl:String = ""

    if (card.suit != CardSuit.NONE) {
        val cardSuitString = card.suit.toString().lowercase()
        val cardValueWeight = if (card.value == CardValue.AS) 1 else card.value.weight.toString()
        imageUrl = "/cardsImages/$cardSuitString/${cardSuitString}_$cardValueWeight.png"
    }else{
        imageUrl = "/cardsImages/card.png"
    }

    Box(
        modifier = Modifier
            //.size(80.dp, 120.dp)
            .size(cardWith.dp, cardHeight.dp)
            .clip(RoundedCornerShape(8.dp))
        //.padding(4.dp)
    ) {
        Image(
            painter = painterResource(imageUrl),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
    Spacer(modifier = Modifier.width(8.dp))
}
