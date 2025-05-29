package gameScreen.wsComm

import kotlinx.serialization.Serializable

@Serializable
data class PlayerDataToShow(
    val name: String,
    var tokenAmount: Int
) {
}