package com.example.fruit_picking_drone_app.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://172.20.10.4:5001/")  // Doğru server adresi
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val harvestApi: HarvestApi by lazy {
        retrofit.create(HarvestApi::class.java)
    }

    val cameraApi: CameraApi by lazy {
        retrofit.create(CameraApi::class.java)
    }
}