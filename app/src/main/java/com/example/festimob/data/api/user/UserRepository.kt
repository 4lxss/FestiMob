package com.example.festimob.data.api.user

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<List<User>>

    fun getUserByID(id_u: Int): Flow<User?>

    suspend fun refreshUsers()
    suspend fun updateUserRole(userId: Int, newRole: String)
    suspend fun deleteUser(userId: Int)

    suspend fun insert(user: User)

    suspend fun update(user: User)

    suspend fun delete(user: User)

}
