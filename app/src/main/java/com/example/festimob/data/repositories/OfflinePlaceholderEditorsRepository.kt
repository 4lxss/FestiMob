package com.example.festimob.data.repositories

//import com.example.festimob.data.api.RetrofitInstance
import com.example.festimob.data.local.LocalDataPlaceholder
import com.example.festimob.data.models.Editor
import com.example.festimob.data.models.EditorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class OfflinePlaceholderEditorsRepository : EditorsRepository {
    override fun getEditorsStream(): Flow<List<Editor>> {
        return flowOf(LocalDataPlaceholder.editorsPlaceholderData)
    }

    override suspend fun insertEditor(editor: Editor) {
        LocalDataPlaceholder.editorsPlaceholderData += editor // Adds into the local list
    }

    override fun getEditorById(id: Int) : Editor {
        LocalDataPlaceholder.editorsPlaceholderData.forEach {
            if (it.id == id) {
                return it
            }
        }
        return Editor(0,"", EditorState.A,false,"",null,null)
    }
}

/*
class OfflineItemsRepository(private val itemDAO: ItemDAO) : ItemsRepository {
    override fun getAllItemsStream(): Flow<List<Item>> = itemDAO.getAllItems()
    override fun getItemStream(id: Int): Flow<Item?> = itemDAO.getItem(id)
    override suspend fun insertItem(item: Item) = itemDAO.insert(item)
    override suspend fun deleteItem(item: Item) = itemDAO.delete(item)
    override suspend fun updateItem(item: Item) = itemDAO.update(item)
}
 */