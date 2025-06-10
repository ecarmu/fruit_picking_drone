package com.example.fruit_picking_drone_app.ui.home

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import com.example.fruit_picking_drone_app.data.repository.HarvestRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _isUserLoggedIn = MutableLiveData(false)
    val isUserLoggedIn: LiveData<Boolean> = _isUserLoggedIn

    private val _summaryText = MutableLiveData("")
    val summaryText: LiveData<String> = _summaryText

    private val _isDarkTheme = MutableLiveData(false)
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    fun loginUser(context: Context, username: String = "User") {
        viewModelScope.launch {
            Log.d("HomeViewModel", "=== LOGIN PROCESS STARTED ===")
            Log.d("HomeViewModel", "Logging in user: $username")
            
            saveLoginStatus(context, true)
            saveUsername(context, username)
            _isUserLoggedIn.value = true
            fetchSummaryData(context)
            
            Log.d("HomeViewModel", "Login completed successfully for user: $username")
            Log.d("HomeViewModel", "=== LOGIN PROCESS ENDED ===")
        }
    }

    fun logoutUser(context: Context) {
        Log.d("HomeViewModel", "=== LOGOUT PROCESS STARTED ===")
        
        // Get current user info before logout for logging
        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val currentUsername = sharedPref.getString("username", "Unknown")
        val wasLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        
        Log.d("HomeViewModel", "Logging out user: $currentUsername (was logged in: $wasLoggedIn)")
        
        // Clear login status
        saveLoginStatus(context, false)
        _isUserLoggedIn.value = false
        
        // Clear username
        with(sharedPref.edit()) {
            remove("username")
            remove("email")
            apply()
        }
        
        Log.d("HomeViewModel", "Logout completed - cleared all user data")
        Log.d("HomeViewModel", "=== LOGOUT PROCESS ENDED ===")
    }

    fun checkLoginStatus(context: Context) {
        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val loggedIn = sharedPref.getBoolean("isLoggedIn", false)
        val username = sharedPref.getString("username", "Unknown")
        
        Log.d("HomeViewModel", "=== CHECKING LOGIN STATUS ===")
        Log.d("HomeViewModel", "Current login status: $loggedIn")
        Log.d("HomeViewModel", "Current username: $username")
        
        _isUserLoggedIn.value = loggedIn

        if (loggedIn) {
            Log.d("HomeViewModel", "User is logged in, fetching summary data")
            fetchSummaryData(context)
        } else {
            Log.d("HomeViewModel", "User is not logged in")
        }
        Log.d("HomeViewModel", "=== END LOGIN STATUS CHECK ===")
    }

    fun saveLoginStatus(context: Context, loggedIn: Boolean) {
        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isLoggedIn", loggedIn)
            apply()
        }
        Log.d("HomeViewModel", "Login status saved: $loggedIn")
    }

    fun saveUsername(context: Context, username: String) {
        val sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("username", username)
            apply()
        }
        Log.d("HomeViewModel", "Username saved: $username")
    }

    fun fetchSummaryData(context: Context) {
        viewModelScope.launch {
            try {
                val repository = HarvestRepository(context)
                val latestHarvest = repository.getLatestHarvest()
                val chestnutCount = repository.getHarvestCount()

                val formattedDate = latestHarvest?.timestamp ?: "Unknown"
                _summaryText.value = "Last Harvest: $formattedDate\nChestnuts Collected: $chestnutCount"
            } catch (e: Exception) {
                _summaryText.value = "Failed to load summary data."
            }
        }
    }
}