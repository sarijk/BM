package com.example.bloommind.scan

import android.content.Context
import android.net.Uri
import java.io.File

fun uriToFile(uri: Uri, context: Context): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
    tempFile.outputStream().use { fileOut ->
        inputStream?.copyTo(fileOut)
    }
    return tempFile
}
