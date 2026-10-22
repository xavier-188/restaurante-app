package com.example.restauranteapp.data

data class Reserva(
    val id: Int = 0,
    val nome: String,
    val telefone: String,
    val data: String,
    val hora: String,
    val pessoas: Int,
    val usuarioId: Int? = null
)
