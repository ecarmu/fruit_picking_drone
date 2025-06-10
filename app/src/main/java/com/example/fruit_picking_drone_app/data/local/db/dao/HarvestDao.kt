package com.example.fruit_picking_drone_app.data.local.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fruit_picking_drone_app.data.local.db.entities.Harvest

@Dao
interface HarvestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHarvest(harvest: Harvest)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(harvests: List<Harvest>)

    @Query("SELECT * FROM harvests ORDER BY timestamp DESC")
    fun getAllHarvestsLive(): LiveData<List<Harvest>>

    @Query("SELECT * FROM harvests ORDER BY timestamp DESC LIMIT 50")
    fun getLast50HarvestsLive(): LiveData<List<Harvest>>

    @Query("SELECT * FROM harvests ORDER BY timestamp DESC")
    suspend fun getAllHarvests(): List<Harvest>

    @Delete
    suspend fun deleteHarvest(harvest: Harvest)

    @Query("SELECT * FROM harvests ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestHarvest(): Harvest?

    @Query("SELECT COUNT(*) FROM harvests")
    suspend fun getHarvestCount(): Int

}
