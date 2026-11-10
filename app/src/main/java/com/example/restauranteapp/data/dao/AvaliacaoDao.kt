package com.example.restauranteapp.data.dao

import android.content.ContentValues
import com.example.restauranteapp.data.local.DatabaseHelper
import com.example.restauranteapp.data.model.Avaliacao

// DAO (Data Access Object): concentra todo o SQL da tabela "avaliacoes" (CRUD)
class AvaliacaoDao(private val helper: DatabaseHelper) {

    // CREATE
    fun inserir(avaliacao: Avaliacao): Long {
        return helper.writableDatabase.insert(
            DatabaseHelper.TABELA_AVALIACOES, null, paraValores(avaliacao)
        )
    }

    // READ (todas as avaliações, mais recentes primeiro)
    fun listar(): List<Avaliacao> {
        val lista = mutableListOf<Avaliacao>()
        val cursor = helper.readableDatabase.query(
            DatabaseHelper.TABELA_AVALIACOES, null, null, null, null, null,
            "${DatabaseHelper.COL_AVALIACAO_ID} DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                val usuarioIdIndex = it.getColumnIndexOrThrow(DatabaseHelper.COL_AVALIACAO_USUARIO_ID)
                lista.add(
                    Avaliacao(
                        id = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COL_AVALIACAO_ID)),
                        autor = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_AVALIACAO_AUTOR)),
                        nota = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COL_AVALIACAO_NOTA)),
                        comentario = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_AVALIACAO_COMENTARIO)),
                        usuarioId = if (it.isNull(usuarioIdIndex)) null else it.getInt(usuarioIdIndex)
                    )
                )
            }
        }
        return lista
    }

    // UPDATE
    fun atualizar(avaliacao: Avaliacao): Int {
        return helper.writableDatabase.update(
            DatabaseHelper.TABELA_AVALIACOES,
            paraValores(avaliacao),
            "${DatabaseHelper.COL_AVALIACAO_ID} = ?",
            arrayOf(avaliacao.id.toString())
        )
    }

    // DELETE
    fun excluir(id: Int): Int {
        return helper.writableDatabase.delete(
            DatabaseHelper.TABELA_AVALIACOES,
            "${DatabaseHelper.COL_AVALIACAO_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun paraValores(avaliacao: Avaliacao): ContentValues {
        val values = ContentValues()
        values.put(DatabaseHelper.COL_AVALIACAO_AUTOR, avaliacao.autor)
        values.put(DatabaseHelper.COL_AVALIACAO_NOTA, avaliacao.nota)
        values.put(DatabaseHelper.COL_AVALIACAO_COMENTARIO, avaliacao.comentario)
        values.put(DatabaseHelper.COL_AVALIACAO_USUARIO_ID, avaliacao.usuarioId)
        return values
    }
}
