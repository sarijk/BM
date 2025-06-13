package com.example.bloommind.crud

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val dao: JournalDao
) : ViewModel() {
    val journals: StateFlow<List<JournalEntity>> = dao.getAllJournals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isSavingJournal = MutableStateFlow(false)
    val isSavingJournal: StateFlow<Boolean> = _isSavingJournal.asStateFlow()

    fun addJournal(journal: JournalEntity) {
        viewModelScope.launch {
            _isSavingJournal.value = true

            dao.insertJournal(journal)

            delay(500)
            _isSavingJournal.value = false
        }

    }

    fun deleteJournalById(id: Int) {
        viewModelScope.launch {
            val journal = dao.getJournalById(id)
            if (journal != null) {
                dao.deleteJournal(journal)
            }
        }
    }

    fun updateJournal(journal: JournalEntity) {
        viewModelScope.launch {
            try {
                dao.updateJournal(journal)
            } catch (e: Exception) {
                Log.e("JournalViewModel", "Gagal update jurnal: ${e.message}")
            }
        }
    }

    fun markAsViewed(id: Int) {
        viewModelScope.launch {
            dao.updateLastViewed(id, System.currentTimeMillis())
        }
    }
}
