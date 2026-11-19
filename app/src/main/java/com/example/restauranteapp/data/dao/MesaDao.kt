package com.example.restauranteapp.data.dao

import android.content.ContentValues
import com.example.restauranteapp.data.local.DatabaseHelper
import com.example.restauranteapp.data.model.Mesa


class MesaDao(private val helper: DatabaseHelper) {


    fun inserir(mesa: Mesa): Long {
        return helper.writableDatabase.insert(
            DatabaseHelper.TABELA_MESAS, null, paraValores(mesa)
        )
    }


    fun listar(): List<Mesa> {
        val lista = mutableListOf<Mesa>()
        val cursor = helper.readableDatabase.query(
            DatabaseHelper.TABELA_MESAS, null, null, null, null, null,
            "${DatabaseHelper.COL_MESA_NUMERO} ASC"
        )
        cursor.use {
            while (it.moveToNext()) {
                lista.add(
                    Mesa(
                        id = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COL_MESA_ID)),
                        numero = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COL_MESA_NUMERO)),
                        lugares = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.COL_MESA_LUGARES)),
                        area = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.COL_MESA_AREA))
                    )
                )
            }
        }
        return lista
    }


    fun atualizar(mesa: Mesa): Int {
        return helper.writableDatabase.update(
            DatabaseHelper.TABELA_MESAS,
            paraValores(mesa),
            "${DatabaseHelper.COL_MESA_ID} = ?",
            arrayOf(mesa.id.toString())
        )
    }


    fun excluir(id: Int): Int {
        return helper.writableDatabase.delete(
            DatabaseHelper.TABELA_MESAS,
            "${DatabaseHelper.COL_MESA_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun paraValores(mesa: Mesa): ContentValues {
        val values = ContentValues()
        values.put(DatabaseHelper.COL_MESA_NUMERO, mesa.numero)
        values.put(DatabaseHelper.COL_MESA_LUGARES, mesa.lugares)
        values.put(DatabaseHelper.COL_MESA_AREA, mesa.area)
        return values
    }
}
