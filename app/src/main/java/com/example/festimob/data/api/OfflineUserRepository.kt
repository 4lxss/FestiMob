package com.example.festimob.data.api

import kotlinx.coroutines.flow.Flow

class OfflineUserRepository(
    private val userDao: UserDao,
    private val apiService: APIService
) : UserRepository {
    override fun getUsers(): Flow<List<User>> = userDao.getUsers()

    override fun getUserByID(id_u: Int): Flow<User?> = userDao.getUserByID(id_u)

    suspend fun refreshUsers() {
        val usersFromNetwork = apiService.getUsers().users
        userDao.insertAll(usersFromNetwork)
    }

    override suspend fun insert(user: User) = userDao.insert(user)

    override suspend fun update(user: User) = userDao.update(user)

    override suspend fun delete(user: User) = userDao.delete(user)
}
