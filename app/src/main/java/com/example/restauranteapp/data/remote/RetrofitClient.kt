package com.example.restauranteapp.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Cria o Retrofit uma única vez (object = singleton) e entrega a implementação da ReceitaApi
object RetrofitClient {

    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    val receitaApi: ReceitaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // converte o JSON em objetos Kotlin
            .build()
            .create(ReceitaApi::class.java)
    }
}
