package com.example.restauranteapp.repository

import android.content.ContentValues
import com.example.restauranteapp.data.local.DatabaseHelper
import com.example.restauranteapp.data.model.Usuario
import android.database.Cursor

class UsuarioRepository(private val helper: DatabaseHelper) {

    fun inserir(usuario: Usuario): Long {
        val values = ContentValues()
        values.put(DatabaseHelper.COL_USUARIO_NOME, usuario.nome)
        values.put(DatabaseHelper.COL_USUARIO_EMAIL, usuario.email)
        values.put(DatabaseHelper.COL_USUARIO_SENHA, usuario.senha)
        return helper.writableDatabase.insert(DatabaseHelper.TABELA_USUARIOS, null, values)
    }

    fun buscarPorEmail(email: String): Usuario? {
        val cursor = helper.readableDatabase.query(
            DatabaseHelper.TABELA_USUARIOS, null,
            "${DatabaseHelper.COL_USUARIO_EMAIL} = ?", arrayOf(email),
            null, null, null
        )
        cursor.use {
            return if (it.moveToFirst()) lerUsuario(it) else null
        }
    }

    fun salvarSessao(usuarioId: Int) {
        val db = helper.writableDatabase
        db.delete(DatabaseHelper.TABELA_SESSAO, null, null)
        val values = ContentValues()
        values.put(DatabaseHelper.COL_SESSAO_ID, 1)
        values.put(DatabaseHelper.COL_SESSAO_USUARIO_ID, usuarioId)
        db.insert(DatabaseHelper.TABELA_SESSAO, null, values)
    }

    fun buscarUsuarioDaSessao(): Usuario? {
        val cursor = helper.readableDatabase.rawQuery(
            """
            SELECT u.* FROM ${DatabaseHelper.TABELA_USUARIOS} u
            INNER JOIN ${DatabaseHelper.TABELA_SESSAO} s
            ON s.${DatabaseHelper.COL_SESSAO_USUARIO_ID} = u.${DatabaseHelper.COL_USUARIO_ID}
            """.trimIndent(),
            null
        )
        cursor.use {
            return if (it.moveToFirst()) lerUsuario(it) else null
        }
    }

    fun encerrarSessao() {
        helper.writableDatabase.delete(DatabaseHelper.TABELA_SESSAO, null, null)
    }

    private fun lerUsuario(cursor: Cursor): Usuario {
        return Usuario(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USUARIO_ID)),
            nome = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USUARIO_NOME)),
            email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USUARIO_EMAIL)),
            senha = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USUARIO_SENHA))
        )
    }
}
