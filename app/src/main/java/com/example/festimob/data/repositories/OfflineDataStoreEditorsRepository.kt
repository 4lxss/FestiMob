package com.example.festimob.data.repositories

import com.example.festimob.data.api.models.editor.AddEditorContactPayload
import com.example.festimob.data.dao.EditorDao
import com.example.festimob.data.dto.toEditor
import com.example.festimob.data.models.Editor
import com.example.festimob.data.models.EditorCreate
import com.example.festimob.data.models.EditorState
import com.example.festimob.data.models.toDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class OfflineDataStoreEditorsRepository(
    private val editorDao : EditorDao
) : EditorsRepository {
    override fun getEditorsStream(): Flow<List<Editor>> {
        return editorDao.getAllEditors().map { dtoList ->
            dtoList.map { dto ->
                dto.toEditor()
            }
        }
    }

    override suspend fun insertEditor(editor: EditorCreate) {
        val current = editorDao.getAllEditors().firstOrNull().orEmpty()
        val nextId = (current.maxOfOrNull { it.id } ?: 0) + 1
        editorDao.insertEditor(
            Editor(
                id = nextId,
                name = editor.name,
                state = EditorState.A,
                present = false,
                bill = "",
                address = com.example.festimob.data.models.Address(
                    street = editor.street,
                    city = editor.city,
                    country = editor.country,
                    postalCode = editor.postalCode
                ),
                imageUrl = null
            ).toDto()
        )
    }

    override suspend fun getEditorById(id: Int): Editor? {
        return editorDao.getAllEditors().firstOrNull()?.firstOrNull { it.id == id }?.toEditor()
    }

    override suspend fun updateEditor(id: Int, editor: Editor, contact: AddEditorContactPayload) {
    }
}
