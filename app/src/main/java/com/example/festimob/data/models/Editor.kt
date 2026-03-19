package com.example.festimob.data.models

//main domain model object
data class Editor(
    val id: Int,
    val name: String,
    val address: Address,
    val state: EditorState,
    val hasPresence: Boolean,
    val billingType: String,
    val imageUrl: String?
)

//address object
data class Address(
    val street: String,
    val city: String,
    val country: String,
    val postalCode: String
)

//workflow management
enum class EditorState {
    A,
    B,
    C;
    //TODO : add real states with joining table

    companion object {
        fun fromString(value: String): EditorState {
            return entries.find { it.name.lowercase() == value.lowercase() } ?: A
        }
    }
}