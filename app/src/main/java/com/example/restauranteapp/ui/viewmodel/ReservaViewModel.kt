package com.example.restauranteapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.restauranteapp.data.model.Reserva
import com.example.restauranteapp.repository.ReservaRepository

class ReservaViewModel(private val repository: ReservaRepository) : ViewModel() {

    var reservas by mutableStateOf(emptyList<Reserva>())
        private set

    // Guarda de quem são as reservas que estão na tela
    private var usuarioId: Int? = null

    // A tela chama isto dentro de um LaunchedEffect, passando o usuário logado
    fun carregar(usuarioId: Int) {
        this.usuarioId = usuarioId
        reservas = repository.listar(usuarioId)
    }

    // Retorna a mensagem de erro, ou null se salvou com sucesso.
    fun salvar(
        id: Int?,
        nome: String,
        telefone: String,
        data: String,
        hora: String,
        pessoas: String,
        observacoes: String
    ): String? {
        val dono = usuarioId ?: return "Faça login para reservar."
        val quantidade = pessoas.trim().toIntOrNull()

        if (nome.isBlank() || telefone.isBlank() || data.isBlank() || hora.isBlank()) {
            return "Preencha nome, telefone, data e horário."
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
            pessoas = quantidade,
            observacoes = observacoes.trim(),
            usuarioId = dono
        )

        if (id == null) {
            repository.inserir(reserva)
        } else {
            repository.atualizar(reserva)
        }
        carregar(dono)
        return null
    }

    fun excluir(id: Int) {
        repository.excluir(id)
        usuarioId?.let { carregar(it) }
    }
}
