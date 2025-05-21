import data.RetrofitServiceFactory
import data.dto.RegistrarUsuarioDTO
import data.dto.UsuarioLoginDTO
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
            println("intentado pillar user info")
            userData = apiService.getUserInfo("Bearer $actualToken")

            try {                println("$userData")
                return@async userData
            }catch(e:Exception){
                return@async userData
            }
        }
    }

}