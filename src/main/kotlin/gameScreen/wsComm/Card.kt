package gameScreen.wsComm

import kotlinx.serialization.Serializable


/**
 * clase para la carta
 */
@Serializable
data class Card(
    val suit:CardSuit,
    val value:CardValue,
) {
}