package data.model

data class Usuario(
    val _id : String?,
    var username: String,
    var password: String,
    var tokens: Int,
    val roles: String? = "USER",
    var isBanned:Boolean = false,
) {
}