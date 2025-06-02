package com.example.fruit_picking_drone_app.data.local.db.dao

import androidx.room.*
import com.example.fruit_picking_drone_app.data.local.db.entities.Drone

@Dao
interface DroneDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrone(drone: Drone)

    @Query("SELECT * FROM drones WHERE droneId = :id")
    suspend fun getDroneById(id: String): Drone?

    @Query("SELECT * FROM drones WHERE pairedUserId = :userId")
    suspend fun getDronesForUser(userId: String): List<Drone>

    @Query("SELECT COUNT(*) FROM drones")
    suspend fun getDroneCount(): Int

    @Delete
    suspend fun deleteDrone(drone: Drone)

    @Query("SELECT * FROM drones LIMIT 1")
    suspend fun getFirstDrone(): Drone?
}