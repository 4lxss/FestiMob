package com.example.festimob.data.api

import android.content.Context
import com.example.festimob.data.UserPreferencesRepository
import com.example.festimob.data.api.reservation.OfflineReservationRepository
import com.example.festimob.data.api.reservation.ReservationRepository
import com.example.festimob.data.api.zone.ZonePlanRepository
import com.example.festimob.dataStore

interface AppContainer {
    val festivalRepository: FestivalRepository
    val userPreferencesRepository: UserPreferencesRepository
    val userRepository : UserRepository

    val zonePlanRepository: ZonePlanRepository
    val reservationRepository: ReservationRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineFestivalRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for |FestivalRepository]
     */
    private val database: ApplicationDatabase by lazy {
        ApplicationDatabase.getDatabase(context)
    }

    override val festivalRepository: FestivalRepository by lazy {
        OfflineFestivalRepository(database.festivalDao(), RetrofitInstance.api)
    }

    override val userRepository: UserRepository by lazy {
        OnlineUserRepository(RetrofitInstance.api)
    }

    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.dataStore)
    }
    override val reservationRepository: ReservationRepository by lazy {
        OfflineReservationRepository(RetrofitInstance.api)
    }

    override val zonePlanRepository: ZonePlanRepository by lazy {
        OfflineZonePlanRepository(RetrofitInstance.api)
    }
}
