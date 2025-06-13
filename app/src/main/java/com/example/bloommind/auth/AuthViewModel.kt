package com.example.bloommind.auth

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    companion object {
        private val NAMA_PANGGILAN_KEY = stringPreferencesKey("nickname")
        private val NAMA_LENGKAP_KEY = stringPreferencesKey("fullname")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val PROFILE_IMAGE_URI_KEY = stringPreferencesKey("profile_image_uri")
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data
        .map { prefs -> prefs[IS_LOGGED_IN] ?: false }

    val nickname: Flow<String> = dataStore.data
        .map { prefs -> prefs[NAMA_PANGGILAN_KEY] ?: "" }

    val profileImageUri: StateFlow<String?> = dataStore.data
        .map { prefs -> prefs[PROFILE_IMAGE_URI_KEY] }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    suspend fun setLoginState(loggedIn: Boolean) {
        try {
            dataStore.edit { prefs ->
                prefs[IS_LOGGED_IN] = loggedIn
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Gagal mengubah login state: ${e.message}")
        }
    }

    suspend fun saveUserAccount(
        nickname: String,
        fullName: String,
        email: String,
        password: String
    ) {
        try {
            dataStore.edit { prefs ->
                prefs[NAMA_PANGGILAN_KEY] = nickname
                prefs[NAMA_LENGKAP_KEY] = fullName
                prefs[EMAIL_KEY] = email
                prefs[PASSWORD_KEY] = password
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Gagal menyimpan akun: ${e.message}")
            throw e
        }
    }

    suspend fun validateUser(email: String, password: String): Boolean {
        return try {
            val prefs = dataStore.data.first()
            prefs[EMAIL_KEY] == email && prefs[PASSWORD_KEY] == password
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Gagal validasi login: ${e.message}")
            false
        }
    }

    suspend fun setProfileImageUri(uri: String) {
        try {
            dataStore.edit { prefs ->
                prefs[PROFILE_IMAGE_URI_KEY] = uri
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Gagal menyimpan URI foto profil: ${e.message}")
        }
    }
}
