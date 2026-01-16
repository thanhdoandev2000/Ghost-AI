package com.example.ghostai.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

interface IAppPreferences {
    val authToken: Flow<String?>
    suspend fun saveAuthToken(token: String)
    suspend fun clearAll()
}

@Singleton
class AppPreferencesImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : IAppPreferences {

    override val authToken: Flow<String?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.AUTH_TOKEN]
        }

    override suspend fun saveAuthToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTH_TOKEN] = token
        }
    }

    override suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private object PreferencesKeys {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
    }
}
