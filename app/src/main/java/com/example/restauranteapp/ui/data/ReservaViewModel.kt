package com.example.restauranteapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.restauranteapp.data.Reserva
import com.example.restauranteapp.data.ReservaRepository

class ReservaViewModel(private val repository: ReservaRepository) : ViewModel() {

    var reservas by mutableStateOf(emptyList<Reserva>())
        private set

    init {
        carregar()
    }

    fun carregar() {
        reservas = repository.listar()
    }

    fun salvar(
        id: Int?,
        nome: String,
        telefone: String,
        data: String,
        hora: String,
        pessoas: String
    ): String? {
        val quantidade = pessoas.toIntOrNull()
        if (nome.isBlank() || telefone.isBlank() || data.isBlank() || hora.isBlank()) {
            return "Preencha todos os campos."
        }
        if (quantidade == null || quantidade < 1) {
            return "Informe um número de pessoas válido."
        }

        val reserva = Reserva(
            id = id ?: 0,
            nome = nome.trim(),
            telefone = telefone.trim(),
            data = data.trim(),
            hora = hora.trim(),
            pessoas = quantidade
        )

        if (id == null) {
            repository.inserir(reserva)
        } else {
            repository.atualizar(reserva)
        }
        carregar()
        return null
    }

    fun excluir(id: Int) {
        repository.excluir(id)
        carregar()
    }
}
