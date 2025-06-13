package com.example.bloommind.scan

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface PredictionApiService {

    @Multipart
    @POST("customvision/v3.0/Prediction/{projectId}/classify/iterations/{iterationName}/image")
    suspend fun predictImage(
        @Header("Prediction-Key") predictionKey: String,
        @Path("projectId") projectId: String,
        @Path("iterationName") iterationName: String,
        @Part image: MultipartBody.Part
    ): PredictionResponse

    companion object {
        fun create(): PredictionApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://southcentralus.api.cognitive.microsoft.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(PredictionApiService::class.java)
        }
    }
}
