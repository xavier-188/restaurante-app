package com.example.restauranteapp.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "restaurante.db"
        const val DATABASE_VERSION = 5

        // Tabela usuarios
        const val TABELA_USUARIOS = "usuarios"
        const val COL_USUARIO_ID = "id"
        const val COL_USUARIO_NOME = "nome"
        const val COL_USUARIO_EMAIL = "email"
        const val COL_USUARIO_SENHA = "senha"

        // Tabela sessao (guarda quem está logado; sempre a linha id = 1)
        const val TABELA_SESSAO = "sessao"
        const val COL_SESSAO_ID = "id"
        const val COL_SESSAO_USUARIO_ID = "usuario_id"

        // Tabela pratos
        const val TABELA_PRATOS = "pratos"
        const val COL_PRATO_ID = "id"
        const val COL_PRATO_NOME = "nome"
        const val COL_PRATO_DESCRICAO = "descricao"
        const val COL_PRATO_PRECO = "preco"
        const val COL_PRATO_CATEGORIA = "categoria"
        const val COL_PRATO_EMOJI = "emoji"
        const val COL_PRATO_FOTO = "foto" // foto tirada com a câmera, guardada como BLOB

        // Tabela reservas
        const val TABELA_RESERVAS = "reservas"
        const val COL_RESERVA_ID = "id"
        const val COL_RESERVA_NOME = "nome"
        const val COL_RESERVA_TELEFONE = "telefone"
        const val COL_RESERVA_DATA = "data"
        const val COL_RESERVA_HORA = "hora"
        const val COL_RESERVA_PESSOAS = "pessoas"
        const val COL_RESERVA_OBSERVACOES = "observacoes"
        const val COL_RESERVA_USUARIO_ID = "usuario_id"

        // Tabela mesas
        const val TABELA_MESAS = "mesas"
        const val COL_MESA_ID = "id"
        const val COL_MESA_NUMERO = "numero"
        const val COL_MESA_LUGARES = "lugares"
        const val COL_MESA_AREA = "area"

        // Tabela avaliacoes
        const val TABELA_AVALIACOES = "avaliacoes"
        const val COL_AVALIACAO_ID = "id"
        const val COL_AVALIACAO_AUTOR = "autor"
        const val COL_AVALIACAO_NOTA = "nota"
        const val COL_AVALIACAO_COMENTARIO = "comentario"
        const val COL_AVALIACAO_USUARIO_ID = "usuario_id"
    }

    // Chamado pelo Android uma única vez, quando o banco ainda não existe
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABELA_USUARIOS (
                $COL_USUARIO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USUARIO_NOME TEXT NOT NULL,
                $COL_USUARIO_EMAIL TEXT NOT NULL UNIQUE,
                $COL_USUARIO_SENHA TEXT NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABELA_SESSAO (
                $COL_SESSAO_ID INTEGER PRIMARY KEY,
                $COL_SESSAO_USUARIO_ID INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABELA_PRATOS (
                $COL_PRATO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PRATO_NOME TEXT NOT NULL,
                $COL_PRATO_DESCRICAO TEXT,
                $COL_PRATO_PRECO REAL NOT NULL,
                $COL_PRATO_CATEGORIA TEXT NOT NULL,
                $COL_PRATO_EMOJI TEXT,
                $COL_PRATO_FOTO BLOB
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABELA_RESERVAS (
                $COL_RESERVA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_RESERVA_NOME TEXT NOT NULL,
                $COL_RESERVA_TELEFONE TEXT NOT NULL,
                $COL_RESERVA_DATA TEXT NOT NULL,
                $COL_RESERVA_HORA TEXT NOT NULL,
                $COL_RESERVA_PESSOAS INTEGER NOT NULL,
                $COL_RESERVA_OBSERVACOES TEXT NOT NULL DEFAULT '',
                $COL_RESERVA_USUARIO_ID INTEGER
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABELA_MESAS (
                $COL_MESA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_MESA_NUMERO INTEGER NOT NULL,
                $COL_MESA_LUGARES INTEGER NOT NULL,
                $COL_MESA_AREA TEXT NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABELA_AVALIACOES (
                $COL_AVALIACAO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_AVALIACAO_AUTOR TEXT NOT NULL,
                $COL_AVALIACAO_NOTA INTEGER NOT NULL,
                $COL_AVALIACAO_COMENTARIO TEXT NOT NULL DEFAULT '',
                $COL_AVALIACAO_USUARIO_ID INTEGER
            )
            """.trimIndent()
        )

        inserirMesa(db, 1, 2, "Salão")
        inserirMesa(db, 2, 4, "Salão")
        inserirMesa(db, 3, 6, "Varanda")

        // Dados iniciais, só para a lista do cardápio não começar vazia (pode remover)
        inserirPrato(db, "Filé à Parmegiana", "Filé empanado, molho de tomate, queijo gratinado e arroz com fritas.", 52.0, "Pratos", "🥩")
        inserirPrato(db, "Risoto de Camarão", "Arroz arbóreo cremoso com camarões salteados e ervas frescas.", 68.0, "Pratos", "🍤")
        inserirPrato(db, "Frango Grelhado", "Peito de frango grelhado, ervas finas e limão siciliano.", 44.0, "Pratos", "🍗")
        inserirPrato(db, "Hambúrguer Bella", "Pão brioche, hambúrguer artesanal, queijo, salada e molho da casa.", 38.0, "Lanches", "🍔")
        inserirPrato(db, "Bruschetta Italiana", "Pão italiano tostado, tomate, manjericão e azeite extravirgem.", 26.0, "Entradas", "🍅")
        inserirPrato(db, "Pudim da Casa", "Pudim cremoso de leite condensado com calda de caramelo.", 18.0, "Sobremesas", "🍮")
    }

    // Chamado quando DATABASE_VERSION aumenta: apaga tudo e recria (simples, mas perde os dados)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABELA_AVALIACOES")
        db.execSQL("DROP TABLE IF EXISTS $TABELA_MESAS")
        db.execSQL("DROP TABLE IF EXISTS $TABELA_RESERVAS")
        db.execSQL("DROP TABLE IF EXISTS $TABELA_PRATOS")
        db.execSQL("DROP TABLE IF EXISTS $TABELA_SESSAO")
        db.execSQL("DROP TABLE IF EXISTS $TABELA_USUARIOS")
        onCreate(db)
    }

    // Usa o "db" recebido no onCreate, nunca writableDatabase (causaria erro dentro do onCreate)
    private fun inserirPrato(
        db: SQLiteDatabase,
        nome: String,
        descricao: String,
        preco: Double,
        categoria: String,
        emoji: String
    ) {
        val values = ContentValues()
        values.put(COL_PRATO_NOME, nome)
        values.put(COL_PRATO_DESCRICAO, descricao)
        values.put(COL_PRATO_PRECO, preco)
        values.put(COL_PRATO_CATEGORIA, categoria)
        values.put(COL_PRATO_EMOJI, emoji)
        db.insert(TABELA_PRATOS, null, values)
    }

    private fun inserirMesa(db: SQLiteDatabase, numero: Int, lugares: Int, area: String) {
        val values = ContentValues()
        values.put(COL_MESA_NUMERO, numero)
        values.put(COL_MESA_LUGARES, lugares)
        values.put(COL_MESA_AREA, area)
        db.insert(TABELA_MESAS, null, values)
    }
}
