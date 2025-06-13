package com.example.bloommind.components

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        if (inputStream == null) {
            Log.e("ImageUtils", "InputStream dari URI null: $uri")
            return null
        }

        val fileName = "profile_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        inputStream.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }

        Log.d("ImageUtils", "Gambar berhasil disimpan di ${file.absolutePath}")
        file.absolutePath
    } catch (e: FileNotFoundException) {
        Log.e("ImageUtils", "File tidak ditemukan: ${e.message}")
        null
    } catch (e: IOException) {
        Log.e("ImageUtils", "Gagal menyimpan gambar: ${e.message}")
        null
    } catch (e: Exception) {
        Log.e("ImageUtils", "Error lain saat simpan gambar: ${e.message}")
        null
    }
}
