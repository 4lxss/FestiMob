package com.example.festimob.data.api.editor

//main domain model object
data class Editor(
    val id: Int,
    val name: String,
    val state: EditorState,
    val present: Boolean,
    val bill: String,
    val address: Address?,
    val imageUrl: String?
)

//address object
data class Address(
    val street: String?,
    val city: String?,
    val country: String?,
    val postalCode: String?
)

fun Editor.toDto(): EditorDto {
    return EditorDto(
        id = this.id,
        name = this.name,
        // Flattening Address object (handling null values)
        street = this.address?.street,
        city = this.address?.city,
        country = this.address?.country,
        postalCode = this.address?.postalCode,
        // Converting the Enum back to a String for the DB
        state = this.state.name,
        presence = this.present,
        facture = this.bill,
        imageUrl = this.imageUrl,
        // Current time as timestamp
        updatedAt = System.currentTimeMillis()
    )
}

//workflow management
enum class EditorState {
    A,
    B,
    C;
    //TODO : add real states with joining table

    companion object {
        fun fromString(value: String): EditorState {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: A
        }
    }
}