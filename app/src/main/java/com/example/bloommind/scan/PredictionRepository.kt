package com.example.bloommind.scan

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class PredictionRepository {

    private val api = PredictionApiService.create()
    private val predictionKey = "888f0bfb07a342958bd17e7b6d7cdbce"
    private val projectId = "f0a8390c-046d-43df-b5c0-4a66df0dcf25"
    private val iterationName = "tomat-v1"

    suspend fun predictDisease(file: File): PredictionResponse {
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val multipart = MultipartBody.Part.createFormData("image", file.name, requestFile)
        return api.predictImage(predictionKey, projectId, iterationName, multipart)
    }
}
