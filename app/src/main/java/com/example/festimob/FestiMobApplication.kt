package com.example.festimob

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.festimob.data.UserPreferencesRepository
import com.example.festimob.data.repositories.EditorsRepository
import java.util.prefs.Preferences

private const val LAYOUT_PREFERENCE_NAME = "layout_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCE_NAME
)

class FestiMobApplication: Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository
    lateinit var editorsRepository: EditorsRepository

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
        editorsRepository = EditorsRepository(dataStore)
    }
}
