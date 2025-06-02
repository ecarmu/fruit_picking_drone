package com.example.fruit_picking_drone_app.ui.drones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fruit_picking_drone_app.data.repository.DroneRepository

class DroneViewModelFactory(
    private val repository: DroneRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DronesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DronesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}