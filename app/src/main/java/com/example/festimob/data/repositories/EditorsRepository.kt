package com.example.festimob.data.repositories

import com.example.festimob.data.dao.EditorDao
import com.example.festimob.data.models.Editor
import kotlinx.coroutines.flow.Flow

interface EditorsRepository {
    fun getEditorsStream(): Flow<List<Editor>>

    suspend fun insertEditor(editor: Editor) : Unit
}