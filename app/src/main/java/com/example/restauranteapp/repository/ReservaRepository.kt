package com.example.restauranteapp.repository

import android.content.ContentValues
import com.example.restauranteapp.data.local.DatabaseHelper
import com.example.restauranteapp.data.model.Reserva

class ReservaRepository(private val helper: DatabaseHelper) {

    fun inserir(reserva: Reserva): Long {
        return helper.writableDatabase.insert(
            DatabaseHelper.TABELA_RESERVAS, null, paraValores(reserva)
        )
    }

    // Lista só as reservas do usuário logado (mais recentes primeiro)
    fun listar(usuarioId: Int): List<Reserva> {
        val lista = mutableListOf<Reserva>()
        val cursor = helper.readableDatabase.query(
            DatabaseHelper.TABELA_RESERVAS, null,
            "${DatabaseHelper.COL_RESERVA_USUARIO_ID} = ?", arrayOf(usuarioId.toString()),
            null, null,
            "${DatabaseHelper.COL_RESERVA_ID} DESC"
        )
        cursor.use {
            while (it.moveToNext()) {
                val usuarioIdIndex = it.getColumnIndexOrThrow(DatabaseHelper.COL_RESERVA_USUARIO_ID)
                lista.add(
                    Reserva(
                        id = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COL_RESERVA_ID)),
                        nome = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_RESERVA_NOME)),
                        telefone = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_RESERVA_TELEFONE)),
                        data = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_RESERVA_DATA)),
                        hora = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_RESERVA_HORA)),
                        pessoas = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COL_RESERVA_PESSOAS)),
                        observacoes = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_RESERVA_OBSERVACOES)),
                        usuarioId = if (it.isNull(usuarioIdIndex)) null else it.getInt(usuarioIdIndex)
                    )
                )
            }
        }
        return lista
    }

    fun atualizar(reserva: Reserva): Int {
        return helper.writableDatabase.update(
            DatabaseHelper.TABELA_RESERVAS,
            paraValores(reserva),
            "${DatabaseHelper.COL_RESERVA_ID} = ?",
            arrayOf(reserva.id.toString())
        )
    }

    fun excluir(id: Int): Int {
        return helper.writableDatabase.delete(
            DatabaseHelper.TABELA_RESERVAS,
            "${DatabaseHelper.COL_RESERVA_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun paraValores(reserva: Reserva): ContentValues {
        val values = ContentValues()
        values.put(DatabaseHelper.COL_RESERVA_NOME, reserva.nome)
        values.put(DatabaseHelper.COL_RESERVA_TELEFONE, reserva.telefone)
        values.put(DatabaseHelper.COL_RESERVA_DATA, reserva.data)
        values.put(DatabaseHelper.COL_RESERVA_HORA, reserva.hora)
        values.put(DatabaseHelper.COL_RESERVA_PESSOAS, reserva.pessoas)
        values.put(DatabaseHelper.COL_RESERVA_OBSERVACOES, reserva.observacoes)
        values.put(DatabaseHelper.COL_RESERVA_USUARIO_ID, reserva.usuarioId)
        return values
    }
}
