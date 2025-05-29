package data.model

data class BetDocument(

    val _id: String?,

    val tableName: String?,
    val tableId: String?,
    val userId: String,
    val amount : Int,
    val type : String,
) {
}