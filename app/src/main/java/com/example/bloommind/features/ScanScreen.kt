package com.example.bloommind.features

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bloommind.components.FocusBoxOverlay
import com.example.bloommind.components.filterPredictions
import com.example.bloommind.components.format
import com.example.bloommind.components.labelMap
import com.example.bloommind.components.suggestionMap
import com.example.bloommind.scan.Prediction
import com.example.bloommind.scan.PredictionCard
import com.example.bloommind.scan.PredictionRepository
import com.example.bloommind.scan.uriToFile
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.net.URLEncoder

@Composable
fun ScanScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var diagnosisResult by remember { mutableStateOf<List<Prediction>>(emptyList()) }
    var imagePreviewUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            imagePreviewUri = it
            val file = try {
                uriToFile(it, context)
            } catch (e: Exception) {
                Toast.makeText(context, "Gagal memuat gambar dari galeri", Toast.LENGTH_SHORT).show()
                Log.e("Gallery", "File error: ${e.message}")
                null
            }

            file?.let { safeFile ->
                scope.launch {
                    isLoading = true
                    try {
                        val result = PredictionRepository().predictDisease(safeFile)
                        diagnosisResult = filterPredictions(result.predictions)

                        if (diagnosisResult.isEmpty()) {
                            Toast.makeText(context, "Gambar tidak terdeteksi penyakit.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: IOException) {
                        Toast.makeText(context, "Cek koneksi internet.", Toast.LENGTH_SHORT).show()
                        Log.e("AzurePrediction", "IO error: ${e.message}")
                    } catch (e: Exception) {
                        Toast.makeText(context, "Gagal menganalisis gambar", Toast.LENGTH_SHORT).show()
                        Log.e("AzurePrediction", "Prediction error: ${e.message}")
                    } finally {
                        isLoading = false
                    }
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            if (imagePreviewUri != null) {
                AsyncImage(
                    model = imagePreviewUri,
                    contentDescription = "Preview dari galeri",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx).apply {
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                        }

                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        imageCapture = ImageCapture.Builder().build()
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageCapture
                            )
                        } catch (e: Exception) {
                            Toast.makeText(ctx, "Kamera gagal dimuat", Toast.LENGTH_SHORT).show()
                            Log.e("CameraX", "Preview error", e)
                        }

                        previewView
                    },
                    modifier = Modifier.matchParentSize()
                )
                FocusBoxOverlay()
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    val photoFile = File(
                        context.cacheDir,
                        "photo_${System.currentTimeMillis()}.jpg"
                    )
                    val uri = Uri.fromFile(photoFile)
                    imagePreviewUri = uri

                    val output = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                    imageCapture?.takePicture(
                        output,
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                scope.launch {
                                    isLoading = true
                                    try {
                                        val result = PredictionRepository().predictDisease(photoFile)
                                        diagnosisResult = filterPredictions(result.predictions)

                                        if (diagnosisResult.isEmpty()) {
                                            Toast.makeText(context, "Tidak terdeteksi penyakit", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: IOException) {
                                        Toast.makeText(context, "Cek koneksi internet", Toast.LENGTH_SHORT).show()
                                        Log.e("AzurePrediction", "IO error: ${e.message}")
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Gagal menganalisis gambar", Toast.LENGTH_SHORT).show()
                                        Log.e("AzurePrediction", "Capture error: ${e.message}")
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }

                            override fun onError(exception: ImageCaptureException) {
                                Toast.makeText(context, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
                                Log.e("CameraX", "Capture failed: ${exception.message}", exception)
                            }
                        }
                    )
                },
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                } else {
                    Text("Ambil Gambar")
                }
            }

            Button(
                onClick = {
                    if (!isLoading) galleryLauncher.launch("image/*")
                },
                enabled = !isLoading
            ) {
                Text("Pilih dari Galeri")
            }
        }

        if (diagnosisResult.isNotEmpty()) {
            val topPrediction = diagnosisResult.first()
            val namaPenyakit = labelMap[topPrediction.tagName] ?: topPrediction.tagName
            val confidence = (topPrediction.probability * 100).format(1)
            val suggestion = suggestionMap[topPrediction.tagName]

            Text(
                text = "Diagnosis utama: $namaPenyakit ($confidence%)",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )

            suggestion?.let {
                Text(
                    text = "\uD83D\uDCA1 Saran: $it",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                items(diagnosisResult, key = { it.tagName }) { prediction ->
                    PredictionCard(prediction = prediction)
                }
            }

            val combinedText = buildString {
                append(namaPenyakit)
                suggestion?.let {
                    append("\n\nSaran: $it")
                }
            }

            Button(
                onClick = {
                    if (imagePreviewUri == null) {
                        Toast.makeText(context, "Gambar tidak tersedia", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val encodedUri = URLEncoder.encode(imagePreviewUri.toString(), "UTF-8")
                    val encodedText = URLEncoder.encode(combinedText, "UTF-8")

                    navController.navigate("jurnal?imageUri=$encodedUri&defaultText=$encodedText")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Simpan ke Jurnal")
            }
        } else if (!isLoading && imagePreviewUri != null) {
            Text(
                text = "Tidak ada hasil diagnosis. Coba gunakan gambar lain.",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
