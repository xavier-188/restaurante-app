package com.example.restauranteapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.restauranteapp.data.model.Reserva
import com.example.restauranteapp.data.dao.ReservaDao
import com.example.restauranteapp.data.local.DatabaseHelper

// AndroidViewModel recebe o application, usado para abrir o banco e criar o DAO (igual ao exemplo da aula)
class ReservaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = ReservaDao(DatabaseHelper(application))

    var reservas by mutableStateOf(emptyList<Reserva>())
        private set

    // Guarda de quem são as reservas que estão na tela
    private var usuarioId: Int? = null

    // A tela chama isto dentro de um LaunchedEffect, passando o usuário logado
    fun carregar(usuarioId: Int) {
        this.usuarioId = usuarioId
        reservas = dao.listar(usuarioId)
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
            dao.inserir(reserva)
        } else {
            dao.atualizar(reserva)
        }
        carregar(dono)
        return null
    }

    fun excluir(id: Int) {
        dao.excluir(id)
        usuarioId?.let { carregar(it) }
    }
}
