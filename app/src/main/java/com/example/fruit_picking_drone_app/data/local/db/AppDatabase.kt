package com.example.fruit_picking_drone_app.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fruit_picking_drone_app.data.local.db.dao.*
import com.example.fruit_picking_drone_app.data.local.db.entities.*

@Database(
    entities = [User::class, Drone::class, Harvest::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun droneDao(): DroneDao
    abstract fun harvestDao(): HarvestDao

    // Singleton instance bu sınıf dışında bir yerde tanımlanacak
}