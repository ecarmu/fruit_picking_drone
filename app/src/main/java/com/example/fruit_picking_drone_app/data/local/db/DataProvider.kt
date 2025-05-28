package com.example.fruit_picking_drone_app.data.local.db

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "fruit_drone_database"
            )
                .fallbackToDestructiveMigration() // versiyon değiştiğinde sıfırla (geliştirme için pratik)
                .build()
            INSTANCE = instance
            instance
        }
    }
}