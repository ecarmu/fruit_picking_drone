package com.example.fruit_picking_drone_app.data.local.db.dao

import androidx.room.*
import com.example.fruit_picking_drone_app.data.local.db.entities.Harvest

@Dao
interface HarvestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHarvest(harvest: Harvest)

    @Query("SELECT * FROM harvests WHERE droneId = :droneId")
    suspend fun getHarvestsByDrone(droneId: String): List<Harvest>

    @Query("SELECT * FROM harvests ORDER BY dateTime DESC")
    suspend fun getAllHarvests(): List<Harvest>

    @Delete
    suspend fun deleteHarvest(harvest: Harvest)
}
