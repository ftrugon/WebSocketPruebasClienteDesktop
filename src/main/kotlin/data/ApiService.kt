package data

import data.dto.RegistrarUsuarioDTO
import data.dto.UsuarioDTO
import data.dto.UsuarioLoginDTO
import data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/users/login")
    suspend fun login(@Body usuarioLoginDTO: UsuarioLoginDTO): LoginResponse

    @POST("/users/register")
    suspend fun register(@Body registrarUsuarioDTO: RegistrarUsuarioDTO): UsuarioDTO
}