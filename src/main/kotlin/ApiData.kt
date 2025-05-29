import data.RetrofitServiceFactory
import data.dto.InsertTableDTO
import data.dto.RegistrarUsuarioDTO
import data.dto.UsuarioLoginDTO
import data.model.BetDocument
import data.model.Table
import data.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async


object ApiData {

    var actualToken:String = ""
    var userData: Usuario? = null


//    init {
//        userData = Usuario("","fran","",40,"")
//    }


    val apiService = RetrofitServiceFactory.retrofitService

    fun getToken(username:String,password:String): Deferred<Pair<String, Boolean>> {

        return CoroutineScope(Dispatchers.IO).async {
            try {
                val token = apiService.login(UsuarioLoginDTO(username, password))
                actualToken = token.token
                println("Token recibido: $actualToken")
                return@async Pair(token.token,true)

            } catch (e: Exception) {
                println("Error en la solicitud: ${e.message}")
                return@async Pair("${e.message}",false)
            }
        }

    }

    fun registrarUsuario(registrarUsuarioDTO: RegistrarUsuarioDTO):Deferred<Pair<String,Boolean>>{

        return CoroutineScope(Dispatchers.IO).async {
            try {
                apiService.register(registrarUsuarioDTO)
                return@async Pair("",true)
            }catch (e:Exception){
                return@async Pair("${e.message}",false)
            }
        }
    }

    fun getALlTables(): Deferred<List<Table>>{
        return CoroutineScope(Dispatchers.IO).async {
            try {
                val tables = apiService.getAllTables("Bearer $actualToken")
                return@async tables
            }catch(e:Exception){
                return@async listOf()
            }
        }
    }

    fun getUserData(): Deferred<Usuario?>{
        return CoroutineScope(Dispatchers.IO).async {
            try {
                userData = apiService.getUserInfo("Bearer $actualToken")
                return@async userData
            }catch(e:Exception){
                println("error en get user data")
                return@async userData
            }
        }
    }

    fun insertTokens(amountToInsert: Int): Deferred<Usuario?>{
        return CoroutineScope(Dispatchers.IO).async {
            try {
                val userDto = apiService.insertTokens("Bearer $actualToken",amountToInsert)
                userData?.tokens = userDto.tokens
                return@async userData
            }catch(e:Exception){
                println("Error inesperado en insert tokens")
                return@async userData
            }
        }
    }


    fun retireTokens(amountToRetire: Int): Deferred<Usuario?>{
        return CoroutineScope(Dispatchers.IO).async {
            try {
                val userDto = apiService.retireTokens("Bearer $actualToken",amountToRetire)
                userData?.tokens = userDto.tokens
                return@async userData
            }catch(e:Exception){
                println("Error inesperado en retire tokens")
                return@async userData
            }
        }
    }

    fun changeUsername(newUsername: String): Deferred<Usuario?>{
        return CoroutineScope(Dispatchers.IO).async {
            try {
                val userDto = apiService.changeUsername("Bearer $actualToken",newUsername)
                userData?.username = userDto.username
                return@async userData
            }catch(e:Exception){
                println("Error inesperado en changeUsername")
                return@async userData
            }
        }
    }

    fun insertTable(tableDTO: InsertTableDTO): Deferred<Table?>{
        return CoroutineScope(Dispatchers.IO).async {
            try {

                return@async apiService.insertTable("Bearer $actualToken",tableDTO)
            }catch(e:Exception){
                println("Error inesperado insert table")
                e.printStackTrace()
                return@async null
            }
        }
    }

    fun getMyBets():Deferred<List<BetDocument>>{
        return CoroutineScope(Dispatchers.IO).async {
            try {
                return@async apiService.getMybets("Bearer $actualToken")
            }catch(e:Exception){
                println("Error inesperado en getMyBets")
                return@async listOf()
            }
        }
    }

}