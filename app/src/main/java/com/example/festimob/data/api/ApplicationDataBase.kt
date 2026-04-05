package com.example.festimob.data.api

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Festival::class], version = 1, exportSchema = false)
@TypeConverters(FestivalConverters::class)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun festivalDao(): FestivalDao

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