package com.example.bloommind.crud

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "mood_preferences")

object MoodPreferenceKeys {
    val SELECTED_MOOD = intPreferencesKey("selected_mood")
}

class MoodPreferenceManager(private val context: Context) {

    val selectedMoodFlow: Flow<Int?> = context.dataStore.data
        .map { preferences: Preferences ->
            preferences[MoodPreferenceKeys.SELECTED_MOOD]
        }

    suspend fun saveSelectedMood(index: Int) {
        context.dataStore.edit { preferences ->
            preferences[MoodPreferenceKeys.SELECTED_MOOD] = index
        }
    }
}
