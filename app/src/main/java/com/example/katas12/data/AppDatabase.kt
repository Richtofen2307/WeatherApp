package com.example.katas12.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.katas12.data.repository.WeatherEntity
import com.example.katas12.data.repository.WeatherDao

@Database(entities = [WeatherEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}