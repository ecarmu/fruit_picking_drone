package com.example.fruit_picking_drone_app.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.fruit_picking_drone_app.data.local.db.DatabaseProvider
import com.example.fruit_picking_drone_app.data.local.db.entities.Harvest
import com.example.fruit_picking_drone_app.data.remote.HarvestDto
import com.example.fruit_picking_drone_app.data.remote.NetworkModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HarvestRepository(private val context: Context) {

    // Room DAO
    private val harvestDao = DatabaseProvider
        .getDatabase(context)
        .harvestDao()

    /** 1. UI katmanının gözlemleyebileceği LiveData listesi */
    fun observeAllHarvests(): LiveData<List<Harvest>> =
        harvestDao.getAllHarvestsLive()

    /** 1b. Son 50 veriyi gözlemle (chart için) */
    fun observeLast50Harvests(): LiveData<List<Harvest>> =
        harvestDao.getLast50HarvestsLive()

    /** 2. Tek kayıt ekleme (local) */
    suspend fun insertHarvest(harvest: Harvest) =
        harvestDao.insertHarvest(harvest)

    /** 3. Tüm kayıtları sil (local) */
    suspend fun clearAll() {
        val list = harvestDao.getAllHarvests()
        list.forEach { harvestDao.deleteHarvest(it) }
    }

    /** 4. En son kaydı getir (local) */
    suspend fun getLatestHarvest(): Harvest? =
        harvestDao.getLatestHarvest()

    /** 5. Toplam kayıt sayısını al (local) */
    suspend fun getHarvestCount(): Int =
        harvestDao.getHarvestCount()

    /** 6. Bulk insert (local) */
    private suspend fun insertAll(harvests: List<Harvest>) =
        harvestDao.insertAll(harvests)

    /**
     * 7. Uzak sunucudan çek, lokal DB'yi güncelle.
     *    @GET("/api/harvests") suspend fun getHarvests(): List<HarvestDto>
     */
    suspend fun refreshAll(userId: String) {
        Log.d("CHART_DEBUG", "HarvestRepository.refreshAll called")
        try {
            Log.d("CHART_DEBUG", "Fetching data from server...")
            val dtoList = withContext(Dispatchers.IO) {
                NetworkModule.harvestApi.getHarvests()
            }
            Log.d("CHART_DEBUG", "Received ${dtoList.size} items from server: $dtoList")
            
            val entities = dtoList.map { dto ->
                Harvest(
                    timestamp = dto.timestamp,
                    count     = dto.count
                )
            }
            Log.d("CHART_DEBUG", "Converted to ${entities.size} entities: $entities")
            
            Log.d("CHART_DEBUG", "Inserting entities into Room database...")
            insertAll(entities)
            Log.d("CHART_DEBUG", "Successfully inserted all entities into Room")
        } catch (e: Exception) {
            Log.e("CHART_DEBUG", "Error in refreshAll", e)
            throw e
        }
    }

    /** 8. Sadece DTO'ları almak için (örn. grafik vs.) */
    suspend fun fetchRemoteHarvests(userId: String): List<HarvestDto> =
        withContext(Dispatchers.IO) {
            NetworkModule.harvestApi.getHarvests()
        }

    /** 9. Yeni bir kaydı uzak sunucuya gönder (POST) */
    suspend fun postHarvest(dto: HarvestDto) =
        withContext(Dispatchers.IO) {
            NetworkModule.harvestApi.postHarvest(dto)
        }
}