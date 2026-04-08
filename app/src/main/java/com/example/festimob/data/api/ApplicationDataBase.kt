package com.example.festimob.data.api

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.festimob.data.api.reservation.Editeur
import com.example.festimob.data.api.reservation.Reservation
import com.example.festimob.data.api.zone.ZoneDao
import com.example.festimob.data.api.zone.ZoneTarif

@Database(entities = [Festival::class, User::class, ZoneTarif::class, Reservation::class, Editeur::class], version = 2, exportSchema = false)
@TypeConverters(FestivalConverters::class)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun festivalDao(): FestivalDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: ApplicationDatabase? = null

        fun getDatabase(context: Context): ApplicationDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ApplicationDatabase::class.java, "application_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
