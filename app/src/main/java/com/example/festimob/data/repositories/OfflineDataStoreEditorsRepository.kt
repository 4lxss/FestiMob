package com.example.festimob.data.repositories

import com.example.festimob.data.dao.EditorDao
import com.example.festimob.data.models.Editor
import kotlinx.coroutines.flow.Flow

class OfflineDataStoreEditorsRepository(
    private val editorDao : EditorDao
) : EditorsRepository {
    override fun getEditorsStream(): Flow<List<Editor>> {
        TODO("Not yet implemented")
    }

    override fun insertEditor(editor: Editor) {
        TODO("Not yet implemented")
    }
}