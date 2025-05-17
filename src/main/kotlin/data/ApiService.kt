package data

import data.dto.RegistrarUsuarioDTO
import data.dto.UsuarioDTO
import data.dto.UsuarioLoginDTO
import data.model.LoginResponse
import data.model.Table
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @POST("/users/login")
    suspend fun login(@Body usuarioLoginDTO: UsuarioLoginDTO): LoginResponse

    @POST("/users/register")
    suspend fun register(@Body registrarUsuarioDTO: RegistrarUsuarioDTO): UsuarioDTO

    @GET("/tables/getAll")
    suspend fun getAllTables(@Header("Authorization") authToken: String): List<Table>

}