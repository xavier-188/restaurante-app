package com.example.restauranteapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.restauranteapp.data.model.Reserva
import com.example.restauranteapp.data.dao.ReservaDao
import com.example.restauranteapp.data.local.DatabaseHelper


class ReservaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = ReservaDao(DatabaseHelper(application))

    var reservas by mutableStateOf(emptyList<Reserva>())
        private set


    private var usuarioId: Int? = null


    fun carregar(usuarioId: Int) {
        this.usuarioId = usuarioId
        reservas = dao.listar(usuarioId)
    }


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
