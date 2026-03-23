package com.example.festimob

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.example.festimob.data.repositories.UserPreferencesRepository
import com.example.festimob.data.repositories.EditorsRepository
import com.example.festimob.data.repositories.OfflineEditorsRepository

private const val LAYOUT_PREFERENCE_NAME = "layout_preferences"
val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCE_NAME
)

class FestiMobApplication: Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository
    lateinit var editorsRepository: EditorsRepository

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
        editorsRepository = OfflineEditorsRepository() //uses offline for now
    }
}
