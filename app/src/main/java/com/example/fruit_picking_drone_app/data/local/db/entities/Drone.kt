package com.example.fruit_picking_drone_app.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drones")
data class Drone(
    @PrimaryKey val droneId: String,
    val name: String,
    val pairedUserId: String
)