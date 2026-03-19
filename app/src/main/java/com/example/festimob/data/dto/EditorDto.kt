package com.example.festimob.data.remote.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.festimob.data.models.Address
import com.example.festimob.data.models.Editor
import com.example.festimob.data.models.EditorState

@Entity(tableName = "editors")
data class EditorDto(
    @PrimaryKey(autoGenerate = false) //db already handles id increment
    @ColumnInfo(name = "id_e")
    val id: Int,
    val name: String,
    val street: String?,
    val city: String?,
    val country: String?,
    @ColumnInfo(name = "postal_code")
    val postalCode: String?,
    val state: String, //etat in db
    val presence: Boolean,
    val facture: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long    //best stored as Long (milliseconds) because timestamp
)

fun EditorDto.toEditor(): Editor {
    return Editor(
        id = this.id,
        name = this.name,
        address = Address(
            street = this.street ?: "",
            city = this.city ?: "",
            country = this.country ?: "",
            postalCode = this.postalCode ?: ""
        ),
        state = EditorState.fromString(this.state),
        hasPresence = this.presence,
        billingType = this.facture,
        imageUrl = this.imageUrl
    )
}
