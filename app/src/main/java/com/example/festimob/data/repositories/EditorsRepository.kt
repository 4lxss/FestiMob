package com.example.festimob.data.repositories

import com.example.festimob.data.types.Editor
import kotlinx.coroutines.flow.Flow

interface EditorsRepository {
    fun getEditorsStream() : Flow<List<Editor>>
    fun getEditorStream(id : Int) : Flow<Editor?>
}