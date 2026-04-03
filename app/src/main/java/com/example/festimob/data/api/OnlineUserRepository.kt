package com.example.festimob.data.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class OnlineUserRepository(
    private val apiService: APIService
) : UserRepository {

    private val usersState = MutableStateFlow<List<User>>(emptyList())

    override fun getUsers(): Flow<List<User>> = usersState

    override fun getUserByID(id_u: Int): Flow<User?> = usersState.map { users ->
        users.firstOrNull { it.id_u == id_u }
    }

    override suspend fun refreshUsers() {
        usersState.value = apiService.getUsers().users
    }

    override suspend fun insert(user: User) {
        // Not used in admin network-only flow.
    }

    override suspend fun update(user: User) {
        // Not used in admin network-only flow.
    }

    override suspend fun delete(user: User) {
        // Not used in admin network-only flow.
    }
}
