package com.example.fruit_picking_drone_app.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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
    abstract fun harvestDao(): HarvestDao
    abstract fun droneDao(): DroneDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fruit_picking_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

    // Singleton instance bu sınıf dışında bir yerde tanımlanacak
}