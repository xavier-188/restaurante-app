package com.example.restauranteapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.restauranteapp.data.model.Avaliacao
import com.example.restauranteapp.data.dao.AvaliacaoDao
import com.example.restauranteapp.data.local.DatabaseHelper

// AndroidViewModel recebe o application, usado para abrir o banco e criar o DAO (igual ao exemplo da aula)
class AvaliacaoViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AvaliacaoDao(DatabaseHelper(application))

    var avaliacoes by mutableStateOf(emptyList<Avaliacao>())
        private set

    fun carregar() {
        avaliacoes = dao.listar()
    }

    // Retorna a mensagem de erro, ou null se salvou com sucesso.
    // O autor é o nome do usuário logado, passado pela tela.
    fun salvar(id: Int?, autor: String, usuarioId: Int?, nota: Int, comentario: String): String? {
        if (nota !in 1..5) {
            return "Escolha uma nota de 1 a 5 estrelas."
        }
        if (comentario.isBlank()) {
            return "Escreva um comentário."
        }

        val avaliacao = Avaliacao(
            id = id ?: 0,
            autor = autor.ifBlank { "Cliente" },
            nota = nota,
            comentario = comentario.trim(),
            usuarioId = usuarioId
        )

        if (id == null) {
            dao.inserir(avaliacao)
        } else {
            dao.atualizar(avaliacao)
        }
        carregar()
        return null
    }

    fun excluir(id: Int) {
        dao.excluir(id)
        carregar()
    }
}
