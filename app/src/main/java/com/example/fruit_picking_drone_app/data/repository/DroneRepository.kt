package com.example.fruit_picking_drone_app.data.repository

import android.content.Context
import com.example.fruit_picking_drone_app.data.local.db.AppDatabase
import com.example.fruit_picking_drone_app.data.local.db.entities.Drone

class DroneRepository(context: Context) {

    private val droneDao = AppDatabase.getDatabase(context).droneDao()

    suspend fun insertDrone(drone: Drone) = droneDao.insertDrone(drone)

    suspend fun getDroneById(droneId: String) = droneDao.getDroneById(droneId)

    suspend fun getDronesForUser(userId: String): List<Drone> = droneDao.getDronesForUser(userId)

    suspend fun getDroneCount() = droneDao.getDroneCount()

    suspend fun deleteDrone(drone: Drone) = droneDao.deleteDrone(drone)

    suspend fun getFirstDrone() = droneDao.getFirstDrone()
}