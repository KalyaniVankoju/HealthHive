package com.example.rtrpproject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun insertUser(user: User,onSuccess:()->Unit) {
        viewModelScope.launch {
            repository.insertUser(user)
            onSuccess()
        }
    }

    fun loginUser(
        username: String,
        password: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val user = repository.login(username, password)
            onResult(user != null)
        }
    }

    fun checkUserExists(
        username: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val user = repository.getUser(username)
            onResult(user != null)
        }
    }
}