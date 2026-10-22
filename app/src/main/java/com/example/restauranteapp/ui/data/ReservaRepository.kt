package com.example.restauranteapp.data

import android.content.ContentValues

class ReservaRepository(private val helper: DatabaseHelper) {

    fun inserir(reserva: Reserva): Long {
        return helper.writableDatabase.insert(
            DatabaseHelper.TABELA_RESERVAS, null, paraValores(reserva)
        )
    }

    fun listar(): List<Reserva> {
        val lista = mutableListOf<Reserva>()
        val cursor = helper.readableDatabase.query(
            DatabaseHelper.TABELA_RESERVAS, null, null, null, null, null,
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
        values.put(DatabaseHelper.COL_RESERVA_USUARIO_ID, reserva.usuarioId)
        return values
    }
}
