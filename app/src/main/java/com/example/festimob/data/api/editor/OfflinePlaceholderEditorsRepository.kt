package com.example.festimob.data.api.editor

//import com.example.festimob.data.api.RetrofitInstance
import com.example.festimob.data.api.local.LocalDataPlaceholder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class OfflinePlaceholderEditorsRepository : EditorsRepository {
    override fun getEditorsStream(): Flow<List<Editor>> {
        return flowOf(LocalDataPlaceholder.editorsPlaceholderData)
    }

    override suspend fun insertEditor(editor: EditorCreate) {
        val nextId = (LocalDataPlaceholder.editorsPlaceholderData.maxOfOrNull { it.id } ?: 0) + 1
        LocalDataPlaceholder.editorsPlaceholderData += Editor(
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
        )
    }

    override suspend fun getEditorById(id: Int): Editor? {
        return LocalDataPlaceholder.editorsPlaceholderData.firstOrNull { it.id == id }
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
