package gameScreen

import kotlinx.serialization.Serializable

@Serializable

enum class CardValue(val weight: Int) {
    TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
    J(11), Q(12), K(13), AS(14), NONE(0)
}