package com.example.bloommind.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel

class ScanViewModel : ViewModel() {
    var scannedImageBitmap by mutableStateOf<ImageBitmap?>(null)
        private set

    fun setScannedImage(bitmap: ImageBitmap) {
        scannedImageBitmap = bitmap
    }

    fun clearImage() {
        scannedImageBitmap = null
    }
}
