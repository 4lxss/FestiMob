package com.example.festimob.data.repositories

import com.example.festimob.data.dao.EditorDao
import kotlinx.coroutines.flow.Flow

interface EditorsRepository {
    fun getEditorsStream() : Flow<List<EditorDao>>
    fun getEditorStream(id : Int) : Flow<EditorDao?>
}