package com.example.fruit_picking_drone_app.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "harvests")
data class Harvest(
    @PrimaryKey val sessionId: String = UUID.randomUUID().toString(),
    val timestamp: Long,
    val count: Int
)