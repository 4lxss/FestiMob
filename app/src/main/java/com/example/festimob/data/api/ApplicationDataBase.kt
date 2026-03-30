package com.example.festimob.data.api

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Festival::class, User::class], version = 2, exportSchema = false)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun festivalDao(): FestivalDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: ApplicationDatabase? = null

        fun getDatabase(context: Context): ApplicationDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ApplicationDatabase::class.java, "application_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
