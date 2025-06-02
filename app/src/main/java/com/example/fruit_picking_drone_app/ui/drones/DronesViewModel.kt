package com.example.fruit_picking_drone_app.ui.drones

import android.util.Log
import androidx.lifecycle.*
import com.example.fruit_picking_drone_app.data.local.db.entities.Drone
import com.example.fruit_picking_drone_app.data.repository.DroneRepository
import kotlinx.coroutines.launch

class DronesViewModel(private val repository: DroneRepository) : ViewModel() {

    private val _drone = MutableLiveData<List<Drone>>()
    val drones: LiveData<List<Drone>> get() = _drone


    private fun loadFirstDrone() {
        viewModelScope.launch {
            repository.getFirstDrone()?.let { firstDrone ->
                _drone.value = listOf(firstDrone)
            } ?: run {
                _drone.value = emptyList()
            }
        }
    }


    fun loadDronesForUser(userId: String) {
        viewModelScope.launch {
            val droneList = repository.getDronesForUser(userId)
            Log.d("ADD_DEBUG", "Loaded ${drones.value} drones for $userId")
            _drone.postValue(droneList)
        }
    }

    fun deleteDrone(drone: Drone) {
        viewModelScope.launch {
            repository.deleteDrone(drone)
            _drone.value = emptyList()
        }
    }

    fun insertDrone(drone: Drone) {
        viewModelScope.launch {
            repository.insertDrone(drone)
            loadDronesForUser(drone.pairedUserId)
        }
    }
}