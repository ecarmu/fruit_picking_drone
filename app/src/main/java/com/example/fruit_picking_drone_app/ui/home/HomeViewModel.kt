package com.example.fruit_picking_drone_app.ui.home

import android.content.Context
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

    fun loginUser(context: Context) {
        viewModelScope.launch {
            saveLoginStatus(context, true)
            _isUserLoggedIn.value = true
            fetchSummaryData(context)
        }
    }

    fun checkLoginStatus(context: Context) {
        val sharedPref = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val loggedIn = sharedPref.getBoolean("isLoggedIn", false)
        _isUserLoggedIn.value = loggedIn

        if (loggedIn) {
            fetchSummaryData(context)
        }
    }

    fun saveLoginStatus(context: Context, loggedIn: Boolean) {
        val sharedPref = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("isLoggedIn", loggedIn)
            apply()
        }
    }

    fun fetchSummaryData(context: Context) {
        viewModelScope.launch {
            try {
                val repository = HarvestRepository(context)
                val latestHarvest = repository.getLatestHarvest()
                val chestnutCount = repository.getHarvestCount()

                val formattedDate = latestHarvest?.dateTime ?: "Unknown"
                _summaryText.value = "Last Harvest: $formattedDate\nChestnuts Collected: $chestnutCount"
            } catch (e: Exception) {
                _summaryText.value = "Failed to load summary data."
            }
        }
    }
}