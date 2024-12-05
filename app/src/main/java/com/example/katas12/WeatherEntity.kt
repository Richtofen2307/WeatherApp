package com.example.katas12

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val id: Int = 0,
    val city: String,
    val temperature: String,
    val description: String,

)
