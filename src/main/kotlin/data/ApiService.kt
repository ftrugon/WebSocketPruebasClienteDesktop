package data

import data.dto.InsertTableDTO
import data.dto.RegistrarUsuarioDTO
import data.dto.UsuarioDTO
import data.dto.UsuarioLoginDTO
import data.model.BetDocument
import data.model.LoginResponse
import data.model.Table
import data.model.Usuario
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("/users/login")
    suspend fun login(@Body usuarioLoginDTO: UsuarioLoginDTO): LoginResponse

    @POST("/users/register")
    suspend fun register(@Body registrarUsuarioDTO: RegistrarUsuarioDTO): UsuarioDTO

    @GET("/users/myInfo")
    suspend fun getUserInfo(@Header("Authorization") authToken: String): Usuario

    @PUT("/changeUsername/{username}")
    suspend fun changeUsername(
        @Header("Authorization") authToken: String,
        @Path("username") username: String,
    ): Usuario

    @PUT("/tokens/insertTokens/{amount}")
    suspend fun insertTokens(
        @Header("Authorization") authHeader: String,
        @Path("amount") amount :Int
    ): UsuarioDTO

    @PUT("/tokens/retireTokens/{amount}")
    suspend fun retireTokens(
        @Header("Authorization") authHeader: String,
        @Path("amount") amount :Int
    ) : UsuarioDTO

    @POST("/tables/insertTable")
    suspend fun insertTable(
        @Header("Authorization") authHeader: String,
        @Body insertTableDTO: InsertTableDTO
    ): Table

    @GET("/tables/getAll")
    suspend fun getAllTables(@Header("Authorization") authToken: String): List<Table>



    @GET("/bets/myBets")
    suspend fun getMybets(@Header("Authorization") authToken: String): List<BetDocument>



}