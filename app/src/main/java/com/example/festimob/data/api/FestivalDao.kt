package com.example.festimob.data.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FestivalDao {
    @Query("SELECT * FROM festival")
    fun getAllFestivals(): Flow<List<Festival>>

    @Query("SELECT * FROM festival WHERE id_f = :id_f")
    fun getFestival(id_f: Int): Flow<Festival>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(festival: Festival)

    @Update
    suspend fun update(festival: Festival)

    @Delete
    suspend fun delete(festival: Festival)

}