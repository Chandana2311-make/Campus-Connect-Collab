package com.example.campusconnectandcollab.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "campus_store")

class AppDataStore(private val context: Context) {

    private val KEY_LOGGED_IN = booleanPreferencesKey("logged_in")
    private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
    private val KEY_EVENTS_JSON = stringPreferencesKey("events_json")

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[KEY_LOGGED_IN] ?: false }
    val userEmail: Flow<String> = context.dataStore.data.map { it[KEY_USER_EMAIL] ?: "" }
    val eventsJson: Flow<String> = context.dataStore.data.map { it[KEY_EVENTS_JSON] ?: "[]" }

    suspend fun setSession(email: String) {
        context.dataStore.edit {
            it[KEY_LOGGED_IN] = true
            it[KEY_USER_EMAIL] = email
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit {
            it[KEY_LOGGED_IN] = false
            it[KEY_USER_EMAIL] = ""
        }
    }

    suspend fun saveEventsJson(json: String) {
        context.dataStore.edit { it[KEY_EVENTS_JSON] = json }
    }
}
