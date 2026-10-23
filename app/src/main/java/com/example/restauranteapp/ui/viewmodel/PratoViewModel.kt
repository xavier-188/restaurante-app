package com.example.restauranteapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.restauranteapp.data.model.Prato
import com.example.restauranteapp.repository.PratoRepository

class PratoViewModel(private val repository: PratoRepository) : ViewModel() {

    var pratos by mutableStateOf(emptyList<Prato>())
        private set

    fun carregar() {
        pratos = repository.listar()
    }

    // Retorna a mensagem de erro, ou null se salvou com sucesso.
    // id == null -> cadastra um prato novo; id != null -> atualiza o existente.
    fun salvar(
        id: Int?,
        nome: String,
        descricao: String,
        preco: String,
        categoria: String,
        emoji: String
    ): String? {
        val valor = preco.trim().replace(",", ".").toDoubleOrNull()
        if (nome.isBlank() || categoria.isBlank()) {
            return "Preencha o nome e escolha a categoria."
        }
        if (valor == null || valor <= 0.0) {
            return "Informe um preço válido (ex: 25,90)."
        }

        val prato = Prato(
            id = id ?: 0,
            nome = nome.trim(),
            descricao = descricao.trim(),
            preco = valor,
            categoria = categoria,
            emoji = emoji.trim().ifEmpty { "🍽️" }
        )

        if (id == null) {
            repository.inserir(prato)
        } else {
            repository.atualizar(prato)
        }
        carregar()
        return null
    }

    fun excluir(id: Int) {
        repository.excluir(id)
        carregar()
    }
}
