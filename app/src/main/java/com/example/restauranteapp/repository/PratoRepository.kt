package com.example.restauranteapp.repository

import android.content.ContentValues
import com.example.restauranteapp.data.local.DatabaseHelper
import com.example.restauranteapp.data.model.Prato

class PratoRepository(private val helper: DatabaseHelper) {

    // CREATE
    fun inserir(prato: Prato): Long {
        return helper.writableDatabase.insert(
            DatabaseHelper.TABELA_PRATOS, null, paraValores(prato)
        )
    }

    // READ
    fun listar(): List<Prato> {
        val lista = mutableListOf<Prato>()
        val cursor = helper.readableDatabase.query(
            DatabaseHelper.TABELA_PRATOS, null, null, null, null, null,
            "${DatabaseHelper.COL_PRATO_ID} ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                lista.add(
                    Prato(
                        id = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COL_PRATO_ID)),
                        nome = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_PRATO_NOME)),
                        descricao = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_PRATO_DESCRICAO)) ?: "",
                        preco = it.getDouble(it.getColumnIndexOrThrow(DatabaseHelper.COL_PRATO_PRECO)),
                        categoria = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_PRATO_CATEGORIA)),
                        emoji = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_PRATO_EMOJI)) ?: ""
                    )
                )
            }
        }
        return lista
    }

    // UPDATE
    fun atualizar(prato: Prato): Int {
        return helper.writableDatabase.update(
            DatabaseHelper.TABELA_PRATOS,
            paraValores(prato),
            "${DatabaseHelper.COL_PRATO_ID} = ?",
            arrayOf(prato.id.toString())
        )
    }

    // DELETE
    fun excluir(id: Int): Int {
        return helper.writableDatabase.delete(
            DatabaseHelper.TABELA_PRATOS,
            "${DatabaseHelper.COL_PRATO_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun paraValores(prato: Prato): ContentValues {
        val values = ContentValues()
        values.put(DatabaseHelper.COL_PRATO_NOME, prato.nome)
        values.put(DatabaseHelper.COL_PRATO_DESCRICAO, prato.descricao)
        values.put(DatabaseHelper.COL_PRATO_PRECO, prato.preco)
        values.put(DatabaseHelper.COL_PRATO_CATEGORIA, prato.categoria)
        values.put(DatabaseHelper.COL_PRATO_EMOJI, prato.emoji)
        return values
    }
}
