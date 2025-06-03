package data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitServiceFactory {

    private const val staticUrl = "http://localhost:8080"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(staticUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    val retrofitService: ApiService by lazy {
        getRetrofit().create(ApiService::class.java)
    }


}