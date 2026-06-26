package com.petcare.app.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object FileHelper {

    /**
     * Copia una imagen desde una URI externa (galería) al almacenamiento interno de la app.
     * Retorna la URI del archivo guardado localmente.
     */
    fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val fileName = "pet_image_${UUID.randomUUID()}.jpg"
            val file = File(context.filesDir, fileName)
            
            val outputStream = FileOutputStream(file)
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            Uri.fromFile(file).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Elimina una imagen del almacenamiento interno.
     */
    fun deleteImage(path: String?) {
        try {
            path?.let {
                val file = File(Uri.parse(it).path ?: "")
                if (file.exists()) file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}