import data.RetrofitServiceFactory
import data.dto.RegistrarUsuarioDTO
import data.dto.UsuarioLoginDTO
import data.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async


object ApiData {

    var Actualtoken:String = ""

    var userData: Usuario? = null

    val apiService = RetrofitServiceFactory.retrofitService

    fun getToken(username:String,password:String): Deferred<Pair<String, Boolean>> {

        return CoroutineScope(Dispatchers.IO).async {
            try {
                val token = apiService.login(UsuarioLoginDTO(username, password))
                print("Token recibido: ${token.token}")
                Actualtoken = token.token
                return@async Pair(token.token,true)

            } catch (e: Exception) {
                print("Error en la solicitud: ${e.message}")
                return@async Pair("${e.message}",false)
            }
        }

    }

    fun registrarUsuario(registrarUsuarioDTO: RegistrarUsuarioDTO):Deferred<Pair<String,Boolean>>{

        return CoroutineScope(Dispatchers.IO).async {
            try {
                val user = apiService.register(registrarUsuarioDTO)
                print("USUARIO $user")
                return@async Pair("",true)
            }catch (e:Exception){
                print("Error en la solicitud: ${e.message}")
                return@async Pair("${e.message}",false)
            }
        }
    }
}