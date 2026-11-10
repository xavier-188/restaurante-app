package com.example.restauranteapp.ui.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.restauranteapp.data.ImagemConversor
import com.example.restauranteapp.data.model.Prato
import com.example.restauranteapp.data.dao.PratoDao
import com.example.restauranteapp.data.local.DatabaseHelper

// AndroidViewModel recebe o application, usado para abrir o banco e criar o DAO (igual ao exemplo da aula)
class PratoViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = PratoDao(DatabaseHelper(application))

    var pratos by mutableStateOf(emptyList<Prato>())
        private set

    fun carregar() {
        pratos = dao.listar()
    }

    // Retorna a mensagem de erro, ou null se salvou com sucesso.
    // id == null -> cadastra um prato novo; id != null -> atualiza o existente.
    fun salvar(
        id: Int?,
        nome: String,
        descricao: String,
        preco: String,
        categoria: String,
        emoji: String,
        fotoNova: Bitmap? // foto tirada agora com a câmera (null = manter a que já tinha)
    ): String? {
        val valor = preco.trim().replace(",", ".").toDoubleOrNull()
        if (nome.isBlank() || categoria.isBlank()) {
            return "Preencha o nome e escolha a categoria."
        }
        if (valor == null || valor <= 0.0) {
            return "Informe um preço válido (ex: 25,90)."
        }

        // Se tirou foto nova, converte para bytes (BLOB); senão mantém a foto que o prato já tinha
        val foto = if (fotoNova != null) {
            ImagemConversor.paraBlob(fotoNova)
        } else {
            pratos.find { it.id == id }?.foto
        }

        val prato = Prato(
            id = id ?: 0,
            nome = nome.trim(),
            descricao = descricao.trim(),
            preco = valor,
            categoria = categoria,
            emoji = emoji.trim().ifEmpty { "🍽️" },
            foto = foto
        )

        if (id == null) {
            dao.inserir(prato)
        } else {
            dao.atualizar(prato)
        }
        carregar()
        return null
    }

    fun excluir(id: Int) {
        dao.excluir(id)
        carregar()
    }
}
