package com.example.restauranteapp.data.model

data class Prato(
    val id: Int = 0,
    val nome: String,
    val descricao: String,
    val preco: Double,
    val categoria: String,
    val emoji: String,
    val foto: ByteArray? = null // foto em bytes (BLOB do banco); null = sem foto
)
