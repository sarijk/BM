package com.example.bloommind.crud

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [JournalEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun journalDao(): JournalDao
}
