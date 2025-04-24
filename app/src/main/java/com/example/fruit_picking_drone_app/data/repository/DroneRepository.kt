package com.example.fruit_picking_drone_app.data.repository

import android.content.Context
import com.example.fruit_picking_drone_app.data.local.db.DatabaseProvider
import com.example.fruit_picking_drone_app.data.local.db.entities.Drone

class DroneRepository(context: Context) {

    private val droneDao = DatabaseProvider.getDatabase(context).droneDao()

    suspend fun insertDrone(drone: Drone) {
        droneDao.insertDrone(drone)
    }

    suspend fun getDroneById(droneId: String): Drone? {
        return droneDao.getDroneById(droneId)
    }

    suspend fun getDronesForUser(userId: String): List<Drone> {
        return droneDao.getDronesForUser(userId)
    }

    suspend fun deleteDrone(drone: Drone) {
        droneDao.deleteDrone(drone)
    }
}