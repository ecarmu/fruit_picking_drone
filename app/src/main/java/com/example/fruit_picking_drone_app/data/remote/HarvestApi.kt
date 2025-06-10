package com.example.fruit_picking_drone_app.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// now matches your server's JSON exactly
data class HarvestDto(

    @SerializedName("timestamp")
    val timestamp: Long,

    // server field is "count"
    @SerializedName("count")
    val count: Int
)

interface HarvestApi {
    @POST("api/harvest")
    suspend fun postHarvest(@Body dto: HarvestDto): Response<Void>

    @GET("api/harvests")
    suspend fun getHarvests(): List<HarvestDto>
}