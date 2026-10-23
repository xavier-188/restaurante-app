package com.example.restauranteapp.data.model

data class Prato(
    val id: Int = 0,
    val nome: String,
    val descricao: String,
    val preco: Double,
    val categoria: String,
    val emoji: String
)
