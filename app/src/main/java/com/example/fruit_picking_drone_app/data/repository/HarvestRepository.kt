package com.example.fruit_picking_drone_app.data.repository

import android.content.Context
import com.example.fruit_picking_drone_app.data.local.db.DatabaseProvider
import com.example.fruit_picking_drone_app.data.local.db.entities.Harvest

class HarvestRepository(context: Context) {

    private val harvestDao = DatabaseProvider.getDatabase(context).harvestDao()

    suspend fun insertHarvest(harvest: Harvest) {
        harvestDao.insertHarvest(harvest)
    }

    suspend fun getHarvestsByDrone(droneId: String): List<Harvest> {
        return harvestDao.getHarvestsByDrone(droneId)
    }

    suspend fun getAllHarvests(): List<Harvest> {
        return harvestDao.getAllHarvests()
    }

    suspend fun deleteHarvest(harvest: Harvest) {
        harvestDao.deleteHarvest(harvest)
    }
}