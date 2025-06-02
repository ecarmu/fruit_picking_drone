package com.example.fruit_picking_drone_app.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.fruit_picking_drone_app.data.local.db.entities.User
import com.example.fruit_picking_drone_app.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserRepository(application)

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    fun loadAllUsers() {
        viewModelScope.launch {
            val list = repository.getAllUsers()
            _users.postValue(list)
        }
    }

    fun insertUser(user: User) {
        viewModelScope.launch {
            repository.insertUser(user)
            loadAllUsers()
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
            loadAllUsers()
        }
    }

    fun getUserById(userId: String, callback: (User?) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserById(userId)
            callback(user)
        }
    }

    fun getUserByUsername(username: String, callback: (User?) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByUsername(username)
            callback(user)
        }
    }
}