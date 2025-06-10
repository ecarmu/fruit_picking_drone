package com.example.fruit_picking_drone_app.data.repository

import com.example.fruit_picking_drone_app.data.remote.HarvestDto
import com.example.fruit_picking_drone_app.data.remote.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepository {
    private val api = NetworkModule.harvestApi

    suspend fun fetchHarvests(): List<HarvestDto> =
        withContext(Dispatchers.IO) {
            api.getHarvests()
        }
}