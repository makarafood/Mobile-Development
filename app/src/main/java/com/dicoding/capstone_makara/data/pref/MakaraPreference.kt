package com.dicoding.capstone_makara.data.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MakaraPreference private constructor(private val dataStore: DataStore<Preferences>){
    suspend fun saveSession(session: MakaraModel) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = session.name
            preferences[TOKEN_KEY] = session.token
            preferences[IS_LOGIN] = session.isLogin
        }
    }

    fun getSession(): Flow<MakaraModel> {
        return dataStore.data.map { preferences ->
            MakaraModel(
                preferences[NAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN] ?: false
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MakaraPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN = booleanPreferencesKey("login")

        fun getInstance(dataStore: DataStore<Preferences>): MakaraPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = MakaraPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}