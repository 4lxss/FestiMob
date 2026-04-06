package com.example.festimob.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.festimob.data.dao.EditorDao
import com.example.festimob.data.dto.EditorDto

/* CRASHES APP
TODO : find why
@Database(entities = [EditorDto::class], version = 1, exportSchema = false) // Use Dto here
abstract class FestiMobDatabase : RoomDatabase() {
    abstract fun editorDao() : EditorDao

    companion object {
        @Volatile
        private var Instance : FestiMobDatabase? = null

        fun getDatabase(context : Context) : FestiMobDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FestiMobDatabase::class.java, "festimob_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
*/