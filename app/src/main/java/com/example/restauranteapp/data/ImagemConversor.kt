package com.example.restauranteapp.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream


object ImagemConversor {

    // Bitmap -> ByteArray
    fun paraBlob(imagem: Bitmap): ByteArray {
        return ByteArrayOutputStream().use { saida ->
            check(imagem.compress(Bitmap.CompressFormat.PNG, 100, saida)) {
                "Não foi possível salvar a imagem."
            }
            saida.toByteArray()
        }
    }

    // ByteArray -> Bitmap
    fun paraBitmap(blob: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(blob, 0, blob.size)
    }
}
