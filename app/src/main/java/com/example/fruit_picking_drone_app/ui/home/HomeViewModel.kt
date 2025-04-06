package com.example.fruit_picking_drone_app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.appcompat.app.AppCompatDelegate


class HomeViewModel : ViewModel() {

    // Kullanıcı giriş durumunu tutan LiveData (false: henüz giriş yapmamış)
    private val _isUserLoggedIn = MutableLiveData<Boolean>(false)
    val isUserLoggedIn: LiveData<Boolean> = _isUserLoggedIn

    // Mini özet verisini tutan LiveData (örneğin son toplama tarihi, toplanan meyve sayısı vs.)
    private val _summaryText = MutableLiveData<String>("")
    val summaryText: LiveData<String> = _summaryText

    // Koyu tema durumu: false = açık tema, true = koyu tema
    private val _isDarkTheme = MutableLiveData<Boolean>(false)
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    // Dummy login fonksiyonu, bunu ayarlayacağım
    fun loginUser() {
        viewModelScope.launch {
            delay(1000)
            _isUserLoggedIn.value = true
            fetchSummaryData()
        }
    }

    // Dummy veritabanı, bunu sonra ayarlayacağım
    fun fetchSummaryData() {
        viewModelScope.launch {
            delay(500)
            _summaryText.value = "Son Toplama Tarihi: 05/04/2025\nToplanan Meyve: 42"
        }
    }

    fun setDarkTheme(isDark: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
