package com.example.bloommind.scan

data class PredictionResponse(
    val predictions: List<Prediction>
)

data class Prediction(
    val tagName: String,
    val probability: Float
)
