package com.example.fruit_picking_drone_app.ui.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.fruit_picking_drone_app.data.local.db.entities.Harvest
import com.example.fruit_picking_drone_app.data.remote.NetworkModule
import kotlinx.coroutines.launch

class DataViewModel(application: Application) : AndroidViewModel(application) {

    private val _harvests = MutableLiveData<List<Harvest>>()
    val harvests: LiveData<List<Harvest>> = _harvests

    /** 2) Call this to pull remote data directly */
    fun refreshHarvests(userId: String) {
        Log.d("CHART_DEBUG", "DataViewModel.refreshHarvests called with userId: $userId")
        viewModelScope.launch {
            try {
                Log.d("CHART_DEBUG", "Fetching data directly from server...")
                val dtoList = NetworkModule.harvestApi.getHarvests()
                Log.d("CHART_DEBUG", "Received ${dtoList.size} items from server")
                
                val entities = dtoList.map { dto ->
                    Harvest(
                        timestamp = dto.timestamp,
                        count = dto.count
                    )
                }
                Log.d("CHART_DEBUG", "Converted to ${entities.size} entities")
                
                _harvests.postValue(entities)
                Log.d("CHART_DEBUG", "Data posted to LiveData successfully")
            } catch (t: Throwable) {
                Log.e("CHART_DEBUG", "Error fetching data from server", t)
                _harvests.postValue(emptyList())
            }
        }
    }
}