package com.example.festimob.data.api.user

enum class Role(val value: String, val priority: Int) {
    USER("user", 0),
    VOLUNTEER("volunteer", 1),
    ORGANIZER("organizer", 2),
    SUPER_ORGANIZER("superOrganizer", 3),
    ADMIN("admin", 4);

    companion object {
        fun fromValue(v: String?) = values().firstOrNull { it.value.equals(v, true) }
    }
}

fun hasMinimumRole(userRole: String?, required: Role): Boolean {
    val r = Role.fromValue(userRole) ?: return false
    return r.priority >= required.priority
}