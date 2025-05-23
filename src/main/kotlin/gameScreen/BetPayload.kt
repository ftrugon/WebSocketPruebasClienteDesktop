package gameScreen

import kotlinx.serialization.Serializable

@Serializable
data class BetPayload(
    val action: BetAction,
    val amount: Int
) {
}