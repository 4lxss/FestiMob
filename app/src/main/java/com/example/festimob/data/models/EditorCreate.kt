package com.example.festimob.data.models

data class EditorCreate(
    val name: String,
    val street: String?,
    val city: String?,
    val country: String?,
    val postalCode: String?,
    val contactFirstName: String,
    val contactLastName: String,
    val contactEmail: String?,
    val contactPhone: String?,
    val contactProfession: String
)
