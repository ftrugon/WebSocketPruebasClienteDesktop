package gameScreen.wsComm

import kotlinx.serialization.Serializable

/**
 * clase para enviar informacion de los jugaddores al cliente
 * @property name el nombre del jugador
 * @property tokenAmount la cantidad de tokens
 */
@Serializable
data class PlayerDataToShow(
    val name: String,
    var tokenAmount: Int
) {
}