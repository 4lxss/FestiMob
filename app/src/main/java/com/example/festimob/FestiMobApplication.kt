package com.example.festimob

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.festimob.data.UserPreferencesRepository
import com.example.festimob.data.api.AppContainer
import com.example.festimob.data.api.AppDataContainer
import com.example.festimob.data.api.FestivalRepository
import com.example.festimob.data.api.OfflineFestivalRepository
import kotlin.getValue
import com.example.festimob.data.api.ApplicationDatabase
import com.example.festimob.data.api.RetrofitInstance

private const val LAYOUT_PREFERENCE_NAME = "layout_preferences"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCE_NAME
)

class FestiMobApplication: Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
