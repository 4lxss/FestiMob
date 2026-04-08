package com.example.festimob

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.festimob.data.api.UserPreferencesRepository
import com.example.festimob.data.api.AppContainer
import com.example.festimob.data.api.AppDataContainer
import com.example.festimob.data.api.RetrofitInstance
import com.example.festimob.data.api.editor.EditorsRepository
import com.example.festimob.data.api.editor.OnlineEditorsRepository

private const val LAYOUT_PREFERENCE_NAME = "layout_preferences"
val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCE_NAME
)

class FestiMobApplication: Application() {
    lateinit var container: AppContainer
    lateinit var editorsRepository: EditorsRepository
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        android.util.Log.d("DEBUG_APP", "FestiMobApplication a bien démarré !")
        container = AppDataContainer(this)
        editorsRepository = OnlineEditorsRepository(RetrofitInstance.api)
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}

