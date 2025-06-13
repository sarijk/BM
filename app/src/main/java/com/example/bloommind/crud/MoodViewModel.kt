package com.example.bloommind.crud

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MoodViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    companion object {
        private val SELECTED_MOOD_INDEX_KEY = intPreferencesKey("selectedMoodIndex")
        private val LAST_MOOD_DATE_KEY = stringPreferencesKey("lastMoodDate")
        private val MOOD_LOGS_KEY = stringPreferencesKey("moodLogs")
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }

    private val _selectedMoodIndex = MutableStateFlow<Int?>(null)
    val selectedMoodIndex: StateFlow<Int?> = _selectedMoodIndex.asStateFlow()

    private val _moodLogs = MutableStateFlow<List<MoodLog>>(emptyList())
    val moodLogs: StateFlow<List<MoodLog>> = _moodLogs.asStateFlow()

    private val _isSavingMood = MutableStateFlow(false)
    val isSavingMood: StateFlow<Boolean> = _isSavingMood.asStateFlow()


    init {
        viewModelScope.launch {
            dataStore.data.firstOrNull()?.let { prefs ->
                val today = LocalDate.now().format(DATE_FORMATTER)
                val lastDate = prefs[LAST_MOOD_DATE_KEY]
                _selectedMoodIndex.value = if (lastDate == today) prefs[SELECTED_MOOD_INDEX_KEY] else null

                val logsJson = prefs[MOOD_LOGS_KEY]
                _moodLogs.value = if (!logsJson.isNullOrEmpty()) {
                    try {
                        Json.decodeFromString(ListSerializer(MoodLog.serializer()), logsJson)
                    } catch (e: Exception) {
                        emptyList()
                    }
                } else emptyList()
            }
        }
    }

    fun setSelectedMood(index: Int) {
        val today = LocalDate.now().format(DATE_FORMATTER)
        viewModelScope.launch {
            _isSavingMood.value = true

            val updatedLogs = _moodLogs.value
                .filterNot { it.date == today }
                .toMutableList()
                .apply { add(MoodLog(date = today, moodIndex = index)) }

            dataStore.edit { prefs ->
                prefs[SELECTED_MOOD_INDEX_KEY] = index
                prefs[LAST_MOOD_DATE_KEY] = today
                prefs[MOOD_LOGS_KEY] = Json.encodeToString(ListSerializer(MoodLog.serializer()), updatedLogs)
            }

            _selectedMoodIndex.value = index
            _moodLogs.value = updatedLogs

            delay(500)
            _isSavingMood.value = false
        }
    }

    fun getLast7DaysMood(): List<MoodLog> {
        val today = LocalDate.now()
        return _moodLogs.value.filter {
            val logDate = LocalDate.parse(it.date, DATE_FORMATTER)
            !logDate.isAfter(today) && !logDate.isBefore(today.minusDays(6))
        }.sortedBy { it.date }
    }

    fun getMoodLogsForLast7Days(): List<MoodLog> {
        val sevenDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
        return _moodLogs.value.filter { it.timestamp >= sevenDaysAgo }
    }
}
