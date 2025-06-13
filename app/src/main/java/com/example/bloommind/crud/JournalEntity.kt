package com.example.bloommind.crud

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journals")
data class JournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val mood: String,
    val content: String,
    val imageUri: String?,
    val timestamp: Long,
    val lastViewed: Long = System.currentTimeMillis()
)
