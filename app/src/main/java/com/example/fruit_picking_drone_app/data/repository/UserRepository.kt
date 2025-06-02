package com.example.fruit_picking_drone_app.data.repository

import android.content.Context
import com.example.fruit_picking_drone_app.data.local.db.DatabaseProvider
import com.example.fruit_picking_drone_app.data.local.db.entities.User

class UserRepository(context: Context) {

    private val userDao = DatabaseProvider.getDatabase(context).userDao()

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun getUserById(userId: String): User? {
        return userDao.getUserById(userId)
    }

    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }
}