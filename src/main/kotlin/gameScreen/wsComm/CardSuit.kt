package gameScreen.wsComm

import kotlinx.serialization.Serializable

@Serializable

enum class CardSuit {
    HEARTS,
    CLUBS,
    SPADES,
    DIAMONDS,
    NONE
}