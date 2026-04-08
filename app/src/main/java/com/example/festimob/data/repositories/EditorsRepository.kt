package com.example.festimob.data.repositories

import com.example.festimob.data.api.models.editor.AddEditorContactPayload
import com.example.festimob.data.models.Editor
import com.example.festimob.data.models.EditorCreate
import kotlinx.coroutines.flow.Flow

interface EditorsRepository {
    fun getEditorsStream(): Flow<List<Editor>>

    suspend fun insertEditor(editor: EditorCreate)
    suspend fun getEditorById(id: Int): Editor?

    suspend fun updateEditor(id: Int, editor: Editor, contact: AddEditorContactPayload)
}
