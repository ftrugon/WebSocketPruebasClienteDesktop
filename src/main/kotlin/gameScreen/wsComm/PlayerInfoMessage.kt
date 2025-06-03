package gameScreen.wsComm

import kotlinx.serialization.Serializable

/**
 * clase para la informaciond de cada jugador que se une a la mesa
 * @property name el nombre de cada jugador
 * @property dinero los tokens del jguaor
 */
@Serializable
data class PlayerInfoMessage(
    val name:String,
    val dinero:Int,
) {
}