package com.example.restauranteapp.data.model

// O JSON da API vem assim: { "meals": [ { "strMeal": "...", "strCategory": "...", ... } ] }
// Os nomes das propriedades são iguais às chaves do JSON para o Gson preencher sozinho.
data class ReceitaResposta(
    val meals: List<Receita>?
)

data class Receita(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String?,
    val strArea: String?,
    val strInstructions: String?
)
