package com.example.festimob.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.festimob.data.dto.EditorDto
import kotlinx.coroutines.flow.Flow

@Dao
interface EditorDao {
    // Returns a Flow of all editors, sorted by name
    // Using Flow ensures the UI stays in sync with the DB automatically
    @Query("SELECT * FROM editors ORDER BY name ASC")
    fun getAllEditors(): Flow<List<EditorDto>>
}
