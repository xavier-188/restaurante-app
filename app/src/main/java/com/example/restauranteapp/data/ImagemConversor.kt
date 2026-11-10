package com.example.restauranteapp.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

// Converte a foto para guardar no banco como BLOB (mesma ideia do ImagemConversor da aula)
object ImagemConversor {

    // Bitmap (foto da câmera) -> ByteArray (vai para a coluna BLOB)
    fun paraBlob(imagem: Bitmap): ByteArray {
        return ByteArrayOutputStream().use { saida ->
            check(imagem.compress(Bitmap.CompressFormat.PNG, 100, saida)) {
                "Não foi possível salvar a imagem."
            }
            saida.toByteArray()
        }
    }

    // ByteArray (lido do banco) -> Bitmap (para mostrar na tela)
    fun paraBitmap(blob: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(blob, 0, blob.size)
    }
}
