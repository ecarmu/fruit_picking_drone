package com.example.fruit_picking_drone_app.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val userId: String,
    val username: String,
    val email: String,
    val password: String
)