package com.example.festimob.data.api

import android.util.Log
import kotlinx.coroutines.flow.Flow

class OfflineUserRepository(
    private val userDao: UserDao,
    private val apiService: APIService
) : UserRepository {
    override fun getUsers(): Flow<List<User>> = userDao.getUsers()

    override fun getUserByID(id_u: Int): Flow<User?> = userDao.getUserByID(id_u)

    suspend fun refreshUsers() {
        try {
            val UsersFromNetwork = apiService.getUsers()
            userDao.insertAll(UsersFromNetwork)

        } catch (e: Exception) {
            Log.e("NetworkRepository", "Erreur réseau, utilisation du mode offline", e)
        }
    }

    override suspend fun insert(user: User) = userDao.insert(user)

    override suspend fun update(user: User) = userDao.update(user)

    override suspend fun delete(user: User) = userDao.delete(user)
}
