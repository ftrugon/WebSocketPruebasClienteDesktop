package gameScreen.wsComm

import kotlinx.serialization.Serializable

/**
 * enum para el palo de la carta
 */
@Serializable
enum class CardSuit {
    HEARTS,
    CLUBS,
    SPADES,
    DIAMONDS,
    NONE
}