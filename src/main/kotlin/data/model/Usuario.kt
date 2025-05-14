package data.model

data class Usuario(
    val _id : String?,
    val username: String,
    var password: String,
    val tokens: Int,
    val roles: String? = "USER"
) {
}