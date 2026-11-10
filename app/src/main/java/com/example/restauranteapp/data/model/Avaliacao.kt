package com.example.restauranteapp.data.model

data class Avaliacao(
    val id: Int = 0,
    val autor: String,
    val nota: Int,
    val comentario: String = "",
    val usuarioId: Int? = null
)
