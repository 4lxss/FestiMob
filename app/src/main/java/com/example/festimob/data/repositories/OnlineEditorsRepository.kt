package com.example.festimob.data.repositories

import com.example.festimob.data.api.APIService
import com.example.festimob.data.api.models.editor.AddEditorContactPayload
import com.example.festimob.data.api.models.editor.AddEditorPayload
import com.example.festimob.data.api.models.editor.AddEditorRequest
import com.example.festimob.data.api.models.editor.EditorResponse
import com.example.festimob.data.models.Address
import com.example.festimob.data.models.Editor
import com.example.festimob.data.models.EditorCreate
import com.example.festimob.data.models.EditorState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class OnlineEditorsRepository(
    private val apiService: APIService
) : EditorsRepository {

    override fun getEditorsStream(): Flow<List<Editor>> =
        flow {
            emit(apiService.getEditors().map { it.toDomain() })
        }.catch { emit(emptyList()) }

    override suspend fun insertEditor(editor: EditorCreate) {
        val request = AddEditorRequest(
            editeur = AddEditorPayload(
                name = editor.name,
                street = editor.street,
                city = editor.city,
                postalCode = editor.postalCode,
                country = editor.country
            ),
            contact = AddEditorContactPayload(
                lastName = editor.contactLastName,
                firstName = editor.contactFirstName,
                email = editor.contactEmail,
                telephone = editor.contactPhone,
                profession = editor.contactProfession
            )
        )
        apiService.addEditor(request)
    }

    override suspend fun getEditorById(id: Int): Editor? {
        return apiService.getEditor(id).toDomain()
    }
}

private fun EditorResponse.toDomain(): Editor {
    val resolvedImageUrl = imageUrl?.takeIf { it.isNotBlank() } ?: imageUrlAlt?.takeIf { it.isNotBlank() }
    return Editor(
        id = id,
        name = name,
        state = EditorState.A,
        present = presence ?: false,
        bill = facture ?: "",
        address = Address(
            street = street,
            city = city,
            country = country,
            postalCode = postalCode
        ),
        imageUrl = resolvedImageUrl
    )
}
