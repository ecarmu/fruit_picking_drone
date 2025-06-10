package com.example.fruit_picking_drone_app.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.fruit_picking_drone_app.data.local.db.entities.Harvest
import com.example.fruit_picking_drone_app.data.repository.HarvestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HarvestViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HarvestRepository(application)

    /** 1. UI’ın doğrudan gözlemleyeceği canlı liste */
    val harvestsLive: LiveData<List<Harvest>> = repository.observeAllHarvests()

    /** 2. Tek seferlik sorgular için MutableLiveData */
    private val _oneShotHarvests = MutableLiveData<List<Harvest>>()
    val oneShotHarvests: LiveData<List<Harvest>> = _oneShotHarvests

    private val _latestHarvest = MutableLiveData<Harvest?>()
    val latestHarvest: LiveData<Harvest?> = _latestHarvest

    private val _harvestCount = MutableLiveData<Int>()
    val harvestCount: LiveData<Int> = _harvestCount

    /**
     * Yeni bir kayıt ekle.  Ekleyince canlı liste (harvestsLive) otomatik güncellenecek.
     */
    fun addHarvest(harvest: Harvest) {
        viewModelScope.launch {
            repository.insertHarvest(harvest)
        }
    }

    /**
     * Room içindeki tüm kayıtları sil.
     */
    fun clearAllHarvests() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }


    /**
     * En son eklenen kaydı al ve latestHarvest’a yaz.
     */
    fun loadLatestHarvest() {
        viewModelScope.launch {
            val h = repository.getLatestHarvest()
            _latestHarvest.postValue(h)
        }
    }

    /**
     * Toplam kayıt sayısını al ve harvestCount’a yaz.
     */
    fun loadHarvestCount() {
        viewModelScope.launch {
            val c = repository.getHarvestCount()
            _harvestCount.postValue(c)
        }
    }

    /**
     * “Pull to refresh” için: önce sunucudan çek, sonra Room’a yaz.
     */
    fun refreshFromServer(userId: String) {
        viewModelScope.launch {
            // IO dispatcher içinde retrofit + Room işlemleri
            withContext(Dispatchers.IO) {
                repository.refreshAll(userId)
            }
        }
    }
}