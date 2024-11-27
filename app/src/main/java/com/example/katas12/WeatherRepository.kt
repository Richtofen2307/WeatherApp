package com.example.katas12

import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi
) {


    suspend fun getCurrentWeather() = weatherApi.getCurrentWeather("your_api_key", "metric", "es")

    suspend fun getForecastWeather() = weatherApi.getForecastWeather("your_api_key", "metric", "es")
}