package com.example.bloommind.crud

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Query("SELECT * FROM journals ORDER BY timestamp DESC")
    fun getAllJournals(): Flow<List<JournalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournal(journal: JournalEntity)

    @Query("SELECT * FROM journals WHERE id = :id LIMIT 1")
    suspend fun getJournalById(id: Int): JournalEntity?

    @Delete
    suspend fun deleteJournal(journal: JournalEntity)

    @Update
    suspend fun updateJournal(journal: JournalEntity)

    @Query("UPDATE journals SET lastViewed = :lastViewed WHERE id = :id")
    suspend fun updateLastViewed(id: Int, lastViewed: Long)

}
