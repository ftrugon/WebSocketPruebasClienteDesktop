import kotlinx.serialization.Serializable

@Serializable

data class Card(
    val suit:CardSuit,
    val value:CardValue,
) {
}