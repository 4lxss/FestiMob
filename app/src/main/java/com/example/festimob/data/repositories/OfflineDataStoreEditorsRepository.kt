package com.example.festimob.data.repositories

import com.example.festimob.data.dao.EditorDao
import com.example.festimob.data.dto.toEditor
import com.example.festimob.data.models.Editor
import kotlinx.coroutines.flow.Flow
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

    override fun insertEditor(editor: Editor) {
        TODO("Not yet implemented")
    }
}