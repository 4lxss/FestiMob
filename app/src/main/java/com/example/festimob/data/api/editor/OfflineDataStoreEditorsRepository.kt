package com.example.festimob.data.api.editor

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
                address = Address(
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
}
