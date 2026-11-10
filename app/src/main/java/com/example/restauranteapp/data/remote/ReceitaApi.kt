package com.example.restauranteapp.data.remote

import com.example.restauranteapp.data.model.ReceitaResposta
import retrofit2.http.GET

// Interface do Retrofit (o "DAO" da API): cada função é um endpoint da API TheMealDB (gratuita, sem chave)
interface ReceitaApi {

    // GET https://www.themealdb.com/api/json/v1/1/random.php -> uma receita aleatória
    @GET("random.php")
    suspend fun buscarReceitaAleatoria(): ReceitaResposta
}
