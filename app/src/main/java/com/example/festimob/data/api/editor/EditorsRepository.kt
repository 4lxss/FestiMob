package com.example.festimob.data.api.editor

import kotlinx.coroutines.flow.Flow

interface EditorsRepository {
    fun getEditorsStream(): Flow<List<Editor>>

    suspend fun insertEditor(editor: EditorCreate)
    suspend fun getEditorById(id: Int): Editor?
}
