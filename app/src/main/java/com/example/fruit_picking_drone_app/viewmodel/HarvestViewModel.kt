package com.example.fruit_picking_drone_app.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.fruit_picking_drone_app.data.local.db.entities.Harvest
import com.example.fruit_picking_drone_app.data.repository.HarvestRepository
import kotlinx.coroutines.launch

class HarvestViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HarvestRepository(application)

    private val _harvestList = MutableLiveData<List<Harvest>>()
    val harvestList: LiveData<List<Harvest>> = _harvestList

    fun loadAllHarvests() {
        viewModelScope.launch {
            val data = repository.getAllHarvests()
            _harvestList.postValue(data)
        }
    }

    fun loadHarvestsByDrone(droneId: String) {
        viewModelScope.launch {
            val data = repository.getHarvestsByDrone(droneId)
            _harvestList.postValue(data)
        }
    }

    fun addHarvest(harvest: Harvest) {
        viewModelScope.launch {
            repository.insertHarvest(harvest)
            loadAllHarvests() // güncelleme sonrası listeyi yenile
        }
    }

    fun deleteHarvest(harvest: Harvest) {
        viewModelScope.launch {
            repository.deleteHarvest(harvest)
            loadAllHarvests() // silme sonrası listeyi yenile
        }
    }
}