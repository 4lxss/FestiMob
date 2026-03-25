package com.example.festimob.data.api

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface FestivalRepository {
    fun getFestivals(): Flow<List<Festival>>

    fun getFestival(id_f: Int): Flow<Festival>

    suspend fun insert(festival: Festival)

    suspend fun update(festival: Festival)

    suspend fun delete(festival: Festival)

}