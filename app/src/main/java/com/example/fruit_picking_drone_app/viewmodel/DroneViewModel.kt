package com.example.fruit_picking_drone_app.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.fruit_picking_drone_app.data.local.db.entities.Drone
import com.example.fruit_picking_drone_app.data.repository.DroneRepository
import kotlinx.coroutines.launch

class DroneViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DroneRepository(application)

    private val _drones = MutableLiveData<List<Drone>>()
    val drones: LiveData<List<Drone>> = _drones

    fun loadDronesForUser(userId: String) {
        viewModelScope.launch {
            val list = repository.getDronesForUser(userId)
            _drones.postValue(list)
        }
    }

    fun insertDrone(drone: Drone) {
        viewModelScope.launch {
            repository.insertDrone(drone)
            loadDronesForUser(drone.pairedUserId)
        }
    }

    fun deleteDrone(drone: Drone) {
        viewModelScope.launch {
            repository.deleteDrone(drone)
            loadDronesForUser(drone.pairedUserId)
        }
    }

    fun getDroneById(droneId: String, callback: (Drone?) -> Unit) {
        viewModelScope.launch {
            val drone = repository.getDroneById(droneId)
            callback(drone)
        }
    }
}