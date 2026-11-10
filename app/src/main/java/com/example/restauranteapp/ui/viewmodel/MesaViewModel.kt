package com.example.restauranteapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.restauranteapp.data.model.Mesa
import com.example.restauranteapp.data.dao.MesaDao
import com.example.restauranteapp.data.local.DatabaseHelper

// AndroidViewModel recebe o application, usado para abrir o banco e criar o DAO (igual ao exemplo da aula)
class MesaViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = MesaDao(DatabaseHelper(application))

    var mesas by mutableStateOf(emptyList<Mesa>())
        private set

    fun carregar() {
        mesas = dao.listar()
    }

    // Retorna a mensagem de erro, ou null se salvou com sucesso.
    // id == null -> cadastra uma mesa nova; id != null -> atualiza a existente.
    fun salvar(id: Int?, numero: String, lugares: String, area: String): String? {
        val numeroMesa = numero.trim().toIntOrNull()
        val quantidade = lugares.trim().toIntOrNull()

        if (numeroMesa == null || numeroMesa < 1) {
            return "Informe um número de mesa válido."
        }
        if (quantidade == null || quantidade < 1) {
            return "Informe a quantidade de lugares."
        }
        if (area.isBlank()) {
            return "Escolha a área da mesa."
        }
        // Não deixa duas mesas com o mesmo número
        if (mesas.any { it.numero == numeroMesa && it.id != id }) {
            return "Já existe a mesa $numeroMesa."
        }

        val mesa = Mesa(id = id ?: 0, numero = numeroMesa, lugares = quantidade, area = area)

        if (id == null) {
            dao.inserir(mesa)
        } else {
            dao.atualizar(mesa)
        }
        carregar()
        return null
    }

    fun excluir(id: Int) {
        dao.excluir(id)
        carregar()
    }
}
