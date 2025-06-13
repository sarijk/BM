package com.example.bloommind.crud

import kotlinx.serialization.Serializable

@Serializable
data class MoodLog(
    val moodIndex: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val date: String,
)